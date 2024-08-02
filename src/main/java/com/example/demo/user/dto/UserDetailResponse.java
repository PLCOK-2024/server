package com.example.demo.user.dto;

import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailResponse {
    private Long id;
    private String email;

    public static UserDetailResponse from(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
