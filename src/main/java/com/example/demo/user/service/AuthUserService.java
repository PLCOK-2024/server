package com.example.demo.user.service;

import com.example.demo.user.dto.SocialLoginRequest;
import com.example.demo.user.dto.SocialLoginResponse;
import com.example.demo.user.dto.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthUserService {
    SocialLoginResponse login(SocialLoginRequest requestDto);

    String getToken(String code, String providerType) throws JsonProcessingException;
}
