package com.plcok.archive;

import com.plcok.AcceptanceTest;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.ArchiveTagResponse;
import com.plcok.archive.dto.TagRequest;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.UserFixture;
import com.plcok.user.UserSteps;
import com.plcok.util.MockMultipartFileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ArchiveCreateAcceptanceTest extends AcceptanceTest {
    @MockBean
    IStorageManager storageManager;

    String token;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    private void assertArchive(ArchiveResponse archive) {
        assertThat(archive.getId()).isNotNull();
        assertThat(archive.getAuthor()).isNotNull();
        assertThat(archive.getPositionX()).isNotNull();
        assertThat(archive.getPositionY()).isNotNull();
        assertThat(archive.getAddress()).isNotNull();
        assertThat(archive.getName()).isNotNull();
        assertThat(archive.getContent()).isNotNull();
        assertThat(archive.getIsPublic()).isNotNull();
        assertThat(archive.getArchiveAttaches()).isNotNull();
        assertThat(archive.getTags()).isNotNull();
    }

    @Test
    public void createArchiveSuccessWithImageAndTag() throws IOException {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var tags = Stream.of("1", "2", "3", "4").map(TagRequest::new).toList();
        request.setTags(tags);
        var archive = ArchiveSteps.createArchive(
                token,
                request,
                MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"),
                MockMultipartFileFixture.mockImageFile(200, 201, "jpeg"),
                MockMultipartFileFixture.mockImageFile(300, 301, "jpeg")
        );

        // archive 검증
        assertArchive(archive);

        // tag 순서 검증
        assertThat(archive.getTags().stream().map(ArchiveTagResponse::getName)).isSorted();

        // file 이미지 사이즈 검증
        var attaches = archive.getArchiveAttaches();
        assertThat(attaches.size()).isEqualTo(3);
        assertThat(attaches.get(0).getWidth()).isEqualTo(100);
        assertThat(attaches.get(0).getHeight()).isEqualTo(101);
        assertThat(attaches.get(1).getWidth()).isEqualTo(200);
        assertThat(attaches.get(1).getHeight()).isEqualTo(201);
        assertThat(attaches.get(2).getWidth()).isEqualTo(300);
        assertThat(attaches.get(2).getHeight()).isEqualTo(301);
    }

    @Test
    public void createArchiveSuccessWithoutImageAndTag() throws IOException {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archive = ArchiveSteps.createArchive(
                token,
                request
        );

        // archive 검증
        assertArchive(archive);

    }
}
