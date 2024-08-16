package com.plcok.user.service;

import com.plcok.user.dto.request.LoginRequest;
import com.plcok.user.dto.response.TokenResponse;

public interface TestAuthService {
    TokenResponse testLogin(LoginRequest request);
}
