package com.example.demo.user.dto;
import com.example.demo.common.rule.Regex;
import com.example.demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
    @Regex(pattern = "^[A-z0-9]+\\@[A-z0-9]+\\.[A-z]+$")
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
