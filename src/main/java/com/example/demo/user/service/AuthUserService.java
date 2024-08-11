package com.example.demo.user.service;

import com.example.demo.user.dto.SocialLoginRequest;
import com.example.demo.user.dto.TokenResponse;

public interface AuthUserService {
    TokenResponse login(SocialLoginRequest requestDto);
}
