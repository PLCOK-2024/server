package com.plcok.user.dto;

import com.plcok.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String profileImage;

    public static UserResponse fromEntity(User user) {
        return fromEntity(user, builder());
    }

    public static <T extends UserResponse> T fromEntity(User user, UserResponse.UserResponseBuilder<T, ?> builder) {
        return builder
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .build();
    }
}
