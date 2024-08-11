package com.example.demo.archive;

import com.example.demo.AcceptanceTest;
import com.example.demo.archive.dto.ArchiveResponse;
import com.example.demo.common.storage.IStorageManager;
import com.example.demo.user.UserFixture;
import com.example.demo.user.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

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
    public void createArchiveSuccess() throws IOException {
        // given & when
        mockForCreateArchive();
        ArchiveResponse response = ArchiveSteps.createArchive(token, ArchiveFixture.defaultCreateArchiveRequest());

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getAuthor()).isNotNull();
    }

    private void mockForCreateArchive() throws IOException {
        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
    }
}
