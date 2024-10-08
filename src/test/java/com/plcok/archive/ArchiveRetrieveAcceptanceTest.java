package com.plcok.archive;

import com.plcok.AcceptanceTest;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.UserFixture;
import com.plcok.user.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchiveRetrieveAcceptanceTest extends AcceptanceTest {
    @MockBean
    IStorageManager storageManager;

    String token;

    String blockedUserToken;

    Long blockedUserId;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();

        blockedUserId = UserSteps.signUp(UserFixture.blockedUserSignupRequest());
        blockedUserToken = UserSteps.testLogin(UserFixture.blockedUserLoginRequest()).getAccessToken();
    }

    @Test
    public void success() throws IOException {
        // given
        mockForCreateArchive();
        ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse songpa = ArchiveSteps.successCreateArchive(token, ArchiveFixture.inSongpaCreateArchiveRequest());
        ArchiveResponse youngdeungpo = ArchiveSteps.successCreateArchive(token, ArchiveFixture.inYoungdeungpoCreateArchiveRequest());

        // when
        List<ArchiveResponse> archives = ArchiveSteps.retrieve(token, ArchiveFixture.defaultRetrieveRequest()).getCollect();

        // then
        assertThat(archives.size()).isEqualTo(2);
        assertThat(archives).noneMatch(archive -> archive.getId().equals(youngdeungpo.getId()));
        assertThat(archives).anyMatch(archive -> archive.getId().equals(songpa.getId()));
    }

    @Test
    public void successWithBlock() throws IOException {
        // given
        mockForCreateArchive();
        ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse blockedArchive = ArchiveSteps.successCreateArchive(blockedUserToken, ArchiveFixture.inSongpaCreateArchiveRequest());

        // when
        UserSteps.block(token, blockedUserId);
        var request = ArchiveFixture.defaultRetrieveRequest();
        request.setBlock(false);
        List<ArchiveResponse> archives = ArchiveSteps.retrieve(token, request).getCollect();

        // then
        assertThat(archives.size()).isEqualTo(1);
        assertThat(archives).noneMatch(archive -> archive.getId().equals(blockedArchive.getId()));
    }

    private void mockForCreateArchive() throws IOException {
        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
    }
}