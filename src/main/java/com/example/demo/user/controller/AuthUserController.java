package com.example.demo.user.controller;

import com.example.demo.user.dto.SocialLoginRequest;
import com.example.demo.user.dto.SocialLoginResponse;
import com.example.demo.user.service.AuthUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증 관련 API")
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/login")
    public ResponseEntity<SocialLoginResponse> login(@RequestBody SocialLoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(requestDto));
    }

    @GetMapping("/user/kakao/callback")
    public ResponseEntity<SocialLoginResponse> kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String accessToken = authUserService.getToken(code, "Kakao");
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(new SocialLoginRequest(accessToken, "Kakao")));
    }

    @GetMapping("/user/google/callback")
    public ResponseEntity<SocialLoginResponse> googleLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String accessToken = authUserService.getToken(code, "Google");
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(new SocialLoginRequest(accessToken, "Google")));
    }
}
