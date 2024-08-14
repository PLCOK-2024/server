package com.example.demo.common.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component("Google")
public class GoogleRequestBodyFactory implements OAuthRequestBodyFactory {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Override
    public MultiValueMap<String, String> createRequestBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("client_secret", CLIENT_SECRET);
        body.add("code", code);
        return body;
    }

    @Override
    public String getRequestUrl() {
        return "https://www.googleapis.com/oauth2/v4/token";
    }
}
