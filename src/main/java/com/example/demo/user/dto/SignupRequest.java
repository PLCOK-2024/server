package com.example.demo.user.dto;

import com.example.demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
