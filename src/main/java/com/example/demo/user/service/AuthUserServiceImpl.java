package com.example.demo.user.service;

import com.example.demo.common.oauth.OAuth2Attributes;
import com.example.demo.common.security.JwtUtil;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserProvider;
import com.example.demo.user.dto.SocialLoginRequest;
import com.example.demo.user.dto.SocialLoginResponse;
import com.example.demo.user.repository.UserProviderRepository;
import com.example.demo.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final UserProviderRepository userProviderRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Override
    @Transactional
    public SocialLoginResponse login(SocialLoginRequest requestDto) {
        String providerType = requestDto.getProviderType();
        String token = requestDto.getAccessToken();
        Map<String, Object> map = (Map<String, Object>) getUserAttributes(providerType, token);
        OAuth2Attributes attributes = OAuth2Attributes.of(providerType, map);
        Optional<UserProvider> userProviderOptional = userProviderRepository
                .findByProviderTypeAndProviderUserId(attributes.getProviderType(), attributes.getProviderUserId());

        User user;
        if (userProviderOptional.isPresent()) {
            user = userProviderOptional.get().getUser();
        } else {
            user = new User();
            userRepository.save(user);
            UserProvider userProvider = UserProvider
                    .of(attributes.getProviderType(), user, attributes.getProviderUserId());
            userProviderRepository.save(userProvider);
        }
        return SocialLoginResponse
                .from(jwtUtil.createToken(user.getId(), user.getRole(), user.getCreatedAt()));
    }

    private Map<?, ?> getUserAttributes(String providerType, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .exchange(getRequestUrl(providerType), HttpMethod.GET, request, String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getRequestUrl(String providerType) {
        if ("Kakao".equals(providerType)) {
            return "https://kapi.kakao.com/v2/user/me";
        } else {
            return "https://www.googleapis.com/oauth2/v2/userinfo";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getToken(String code, String providerType) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", providerType.equals("Kakao") ? CLIENT_ID : GOOGLE_CLIENT_ID);
        body.add("redirect_uri", providerType.equals("Kakao") ? REDIRECT_URI : GOOGLE_REDIRECT_URI);
        body.add("client_secret", providerType.equals("Kakao") ? CLIENT_SECRET : GOOGLE_CLIENT_SECRET);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> tokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                getUrl(providerType),
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private String getUrl(String providerType) {
        if ("Kakao".equals(providerType)) {
            return "https://kauth.kakao.com/oauth/token";
        } else {
            return "https://www.googleapis.com/oauth2/v4/token";
        }
    }
}
