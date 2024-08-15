package com.plcok.user.service;

import com.plcok.user.dto.LoginRequest;
import com.plcok.user.dto.TokenResponse;

public interface TestAuthService {
    TokenResponse testLogin(LoginRequest request);
}
