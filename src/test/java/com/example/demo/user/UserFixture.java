package com.example.demo.user;

import com.plcok.user.dto.request.LoginRequest;
import com.plcok.user.dto.request.SignupRequest;

public class UserFixture {
    public static final String DEFAULT_EMAIL = "vegielcl@gmail.com";
    public static final String DEFAULT_PASSWORD = "password";

    public static SignupRequest defaultSignupRequest() {
        return new SignupRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static LoginRequest defaultLoginRequest() {
        return new LoginRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }
}
