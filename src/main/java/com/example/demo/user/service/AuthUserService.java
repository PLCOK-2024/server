package com.example.demo.user.service;

import com.example.demo.common.oauth.OAuthRequestBodyFactory;
import com.example.demo.user.dto.SocialLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthUserService {
    SocialLoginResponse login(String token, OAuthRequestBodyFactory factory);

    String getToken(String code, OAuthRequestBodyFactory factory) throws JsonProcessingException;
}
