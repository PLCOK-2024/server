package com.example.demo.common.oauth;

import com.example.demo.common.error.BusinessException;
import com.example.demo.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2Attributes {

    private String email;

    private String providerType;

    private String providerUserId;

    public static OAuth2Attributes of(String providerType, Map<String, Object> attributes) {
        if ("Kakao".equals(providerType)) {
            return ofKakao(attributes);
        } else if ("Google".equals(providerType)) {
            return ofGoogle(attributes);
        }

        throw new BusinessException(ErrorCode.PROVIDER_NOT_VALID);
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
//        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        String email = String.valueOf(kakaoAccount.get("email"));
        String providerUserId = String.valueOf(attributes.get("id"));

        return builder()
//                .email(email)
                .providerType("Kakao")
                .providerUserId(providerUserId)
                .build();
    }

    private static OAuth2Attributes ofGoogle(Map<String, Object> attributes) {
        String email = String.valueOf(attributes.get("email"));
        String providerUserId = String.valueOf(attributes.get("id"));

        return builder()
                .email(email)
                .providerType("Google")
                .providerUserId(providerUserId)
                .build();
    }
}