package com.example.demo.user;

import com.example.demo.AcceptanceTest;
import com.example.demo.user.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void signUpSuccess() {
        // given & when
        Long userId = UserSteps.signUp(UserFixture.defaultSignupRequest());

        // then
        assertThat(userId).isNotNull();
    }

    @Test
    public void loginSuccess() {
        // given
        UserSteps.signUp(UserFixture.defaultSignupRequest());

        // when
        TokenResponse response = UserSteps.testLogin(UserFixture.defaultLoginRequest());

        // then
        assertThat(response.getAccessToken()).isNotNull();
    }
}
