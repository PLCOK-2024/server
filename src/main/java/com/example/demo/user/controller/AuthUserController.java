package com.example.demo.user.controller;

import com.example.demo.user.dto.SocialLoginRequest;
import com.example.demo.user.dto.TokenResponse;
import com.example.demo.user.service.AuthUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증 관련 API")
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody SocialLoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(requestDto));
    }
}
