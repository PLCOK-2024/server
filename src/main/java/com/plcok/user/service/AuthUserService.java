package com.plcok.user.service;

import com.plcok.common.oauth.OAuthRequestBodyFactory;
import com.plcok.user.dto.SocialLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthUserService {
    SocialLoginResponse login(String token, OAuthRequestBodyFactory factory);

    String getToken(String code, OAuthRequestBodyFactory factory) throws JsonProcessingException;
}
