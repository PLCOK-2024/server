package com.plcok.archive;

import com.plcok.AcceptanceTest;
import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.common.storage.IStorageManager;
import com.plcok.folder.FolderFixture;
import com.plcok.folder.FolderSteps;
import com.plcok.user.UserFixture;
import com.plcok.user.UserSteps;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.util.MockMultipartFileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ArchiveGetByFolderTest extends AcceptanceTest {

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

    @Test
    public void getByFolder() throws IOException {
        var folder1 = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1));
        var folder2 = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(2));

        for (int i = 1; i <= 10; i++) {
            var request = ArchiveFixture.createArchiveRequest(i);
            var archiveResponse = ArchiveSteps.successCreateArchive(
                    token,
                    request,
                    MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"),
                    MockMultipartFileFixture.mockImageFile(200, 201, "jpeg"),
                    MockMultipartFileFixture.mockImageFile(300, 301, "jpeg"));

            if (i <= 5) {
                FolderSteps.addArchiveToFolder(token, folder1.getId(), archiveResponse.getId());
            } else {
                FolderSteps.addArchiveToFolder(token, folder2.getId(), archiveResponse.getId());
            }
        }

        var archives = ArchiveSteps.getByFolder(token, 1);
        assertThat(archives.getCollect().size()).isEqualTo(5);
        for (int i = 1; i <= 5; i++) {
            assertThat(archives.getCollect().get(i - 1).getName()).isEqualTo("킨더커피" + i);
            assertThat(archives.getCollect().get(i - 1).getContent()).isEqualTo("킨더커피 크렘브륄레 마카롱 맛있어요" + i);
            assertThat(archives.getCollect().get(i - 1).getAddress()).isEqualTo("서울특별시 송파구 석촌호수로 135" + i);
            assertThat(archives.getCollect().get(i - 1).getTags().size()).isEqualTo(i);
        }
    }
}
