package com.plcok.archive;

import com.plcok.AcceptanceTest;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.ArchiveTagResponse;
import com.plcok.archive.dto.TagRequest;
import com.plcok.archive.entity.ArchiveTag;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.UserFixture;
import com.plcok.user.UserSteps;
import com.plcok.util.MockMultipartFileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchiveAcceptanceTest extends AcceptanceTest {
    @MockBean
    IStorageManager storageManager;

    String token;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    @Test
    public void findNearArchivesSuccess() throws IOException {
        // given
        mockForCreateArchive();
        ArchiveSteps.createArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse withinRadius = ArchiveSteps.createArchive(token, ArchiveFixture.withinRadiusCreateArchiveRequest());
        ArchiveResponse outsideRadius = ArchiveSteps.createArchive(token, ArchiveFixture.outsideRadiusCreateArchiveRequest());

        // when
        List<ArchiveResponse> archives = ArchiveSteps.findNearArchives(token, 37.5071, 127.0907).getCollect();

        // then
        assertThat(archives.size()).isEqualTo(2);
        assertThat(archives).noneMatch(archive -> archive.getId().equals(outsideRadius.getId()));
        assertThat(archives).anyMatch(archive -> archive.getId().equals(withinRadius.getId()));
    }

    private void mockForCreateArchive() throws IOException {
        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
    }
}
