package com.plcok.user.dto.request;
import com.plcok.common.rule.Regex;
import com.plcok.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
