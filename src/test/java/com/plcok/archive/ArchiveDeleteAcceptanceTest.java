package com.plcok.archive;

import com.plcok.AcceptanceTest;
import com.plcok.common.error.ErrorCode;
import com.plcok.common.error.ErrorResponse;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.UserFixture;
import com.plcok.user.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ArchiveDeleteAcceptanceTest extends AcceptanceTest {

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
    public void deleteArchive() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archiveResponse = ArchiveSteps.successCreateArchive(token, request);
        ArchiveSteps.successDeleteArchive(token, archiveResponse.getId());
        var error = ArchiveSteps.failRetrieveById(token, archiveResponse.getId());
        assertThat(error.getCode()).isEqualTo(ErrorCode.ENTITY_NOT_FOUND.getCode());
    }

    @Test
    public void deleteArchive_failNotAuthorized() {
        UserSteps.signUp(UserFixture.blockedUserSignupRequest());
        String otherToken = UserSteps.testLogin(UserFixture.blockedUserLoginRequest()).getAccessToken();
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archiveResponse = ArchiveSteps.successCreateArchive(token, request);
        var error = ArchiveSteps.failDeleteArchive(otherToken, archiveResponse.getId());
        assertThat(error.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED.getCode());
    }
}
