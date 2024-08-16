package com.plcok.user.dto.response;

import com.plcok.user.entity.User;
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
