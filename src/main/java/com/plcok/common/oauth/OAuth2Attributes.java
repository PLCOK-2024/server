package com.plcok.common.oauth;

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

    public static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
//        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        String email = String.valueOf(kakaoAccount.get("email"));
        String providerUserId = String.valueOf(attributes.get("id"));

        return builder()
//                .email(email)
                .providerType("Kakao")
                .providerUserId(providerUserId)
                .build();
    }

    public static OAuth2Attributes ofGoogle(Map<String, Object> attributes) {
        String email = String.valueOf(attributes.get("email"));
        String providerUserId = String.valueOf(attributes.get("id"));

        return builder()
                .email(email)
                .providerType("Google")
                .providerUserId(providerUserId)
                .build();
    }
}