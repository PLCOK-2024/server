package com.plcok.user.service;

import com.plcok.common.oauth.OAuth2Attributes;
import com.plcok.common.oauth.OAuthRequestBodyFactory;
import com.plcok.user.entity.UserProvider;
import com.plcok.common.security.JwtUtil;
import com.plcok.user.entity.User;
import com.plcok.user.dto.SocialLoginResponse;
import com.plcok.user.repository.UserProviderRepository;
import com.plcok.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final UserProviderRepository userProviderRepository;

    @Override
    @Transactional
    public SocialLoginResponse login(String token, OAuthRequestBodyFactory factory) {
        Map<String, Object> map = getUserAttributes(factory, token);
        OAuth2Attributes attributes = factory.createOauthAttribute(map);

        User user = findOrCreateUser(attributes);
        return SocialLoginResponse
                .from(jwtUtil.createToken(user.getId(), user.getRole(), user.getCreatedAt()));
    }

    private User findOrCreateUser(OAuth2Attributes attributes) {
        return userProviderRepository.findByProviderTypeAndProviderUserId(attributes.getProviderType().name(), attributes.getProviderUserId())
                .map(UserProvider::getUser)
                .orElseGet(() -> createUser(attributes));
    }

    private User createUser(OAuth2Attributes attributes) {
        User user = new User();
        userRepository.save(user);
        UserProvider userProvider = UserProvider.of(attributes.getProviderType(), user, attributes.getProviderUserId());
        userProviderRepository.save(userProvider);
        return user;
    }

    private Map<String, Object> getUserAttributes(OAuthRequestBodyFactory factory, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .exchange(factory.getUserInfoRequestUrl(), HttpMethod.GET, request, String.class);
        return parseResponseBody(response.getBody());
    }

    private Map<String, Object> parseResponseBody(String responseBody) {
        try {
            return new ObjectMapper().readValue(responseBody, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse user attributes", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getToken(String code, OAuthRequestBodyFactory factory) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = factory.createRequestBody(code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                factory.getRequestUrl(),
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
}
