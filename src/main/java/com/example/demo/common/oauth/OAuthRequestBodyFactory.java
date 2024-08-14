package com.example.demo.common.oauth;

import org.springframework.util.MultiValueMap;

public interface OAuthRequestBodyFactory {

    MultiValueMap<String, String> createRequestBody(String code);

    String getRequestUrl();
}
