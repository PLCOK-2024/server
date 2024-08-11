package com.example.demo.user.service;

import com.example.demo.user.dto.SocialLoginRequest;
import com.example.demo.user.dto.TokenResponse;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    private final String KAKAO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

    private final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Override
    @Transactional
    public TokenResponse login(SocialLoginRequest requestDto) {
        String oAuthAccessToken = requestDto.getAccessToken();
        String provider = requestDto.getProvider();
        String reqURL = getReqUrl(provider);
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + oAuthAccessToken);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getReqUrl(String provider) {
        if ("Kakao".equals(provider)) {
            return KAKAO_REQUEST_URL;
        } else {
            return GOOGLE_REQUEST_URL;
        }
    }
}
