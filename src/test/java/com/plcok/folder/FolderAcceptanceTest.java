package com.plcok.folder;

import com.plcok.AcceptanceTest;
import com.plcok.archive.ArchiveFixture;
import com.plcok.archive.ArchiveSteps;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.UserFixture;
import com.plcok.user.UserSteps;
import com.plcok.user.dto.response.FolderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class FolderAcceptanceTest extends AcceptanceTest {

    @MockBean
    IStorageManager storageManager;

    String token;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    private void mockForCreateArchive() throws IOException {
        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
    }

    @Test
    public void createFolderSuccess() {
        // given && when
        FolderResponse response = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1));

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("folder1");
        assertThat(response.getCount()).isEqualTo(0);
    }

    @Test
    public void getFolderListSuccess() {
        // given
        for (int i = 1; i <= 5; i++) {
            FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(i));
        }

        // when
        List<FolderResponse> folders = FolderSteps.getFolderList(token).getFolders();

        // then
        assertThat(folders.size()).isEqualTo(5);
    }

    @Test
    public void addArchiveToFolderSuccess() throws IOException {
        // given
        mockForCreateArchive();
        ArchiveResponse archiveResponse1 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse archiveResponse2 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        FolderResponse folderResponse = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1));

        // when
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveResponse1.getId());
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveResponse2.getId());
        List<FolderResponse> folders = FolderSteps.getFolderList(token).getFolders();

        // then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0).getCount()).isEqualTo(2);
    }

    @Test
    public void removeArchiveToFolderSuccess() throws IOException {
        // given
        mockForCreateArchive();
        ArchiveResponse archiveResponse1 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse archiveResponse2 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        FolderResponse folderResponse = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1));
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveResponse1.getId());
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveResponse2.getId());

        // when
        FolderSteps.removeArchiveToFolder(token, folderResponse.getId(), archiveResponse1.getId());
        List<FolderResponse> folders = FolderSteps.getFolderList(token).getFolders();

        // then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0).getCount()).isEqualTo(1);
    }
}
