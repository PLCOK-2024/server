package com.plcok.user.controller;

import com.plcok.user.dto.SocialLoginRequest;
import com.plcok.user.dto.SocialLoginResponse;
import com.plcok.user.service.AuthUserService;
import com.plcok.common.oauth.GoogleRequestBodyFactory;
import com.plcok.common.oauth.KakaoRequestBodyFactory;
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
    private final GoogleRequestBodyFactory googleRequestBodyFactory;
    private final KakaoRequestBodyFactory kakaoRequestBodyFactory;

    @PostMapping("/kakao/login")
    public ResponseEntity<SocialLoginResponse> kakaoLogin(@RequestBody SocialLoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(requestDto.getAccessToken(), kakaoRequestBodyFactory));
    }

    @PostMapping("/google/login")
    public ResponseEntity<SocialLoginResponse> googleLogin(@RequestBody SocialLoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(requestDto.getAccessToken(), googleRequestBodyFactory));
    }

    @GetMapping("/user/kakao/callback")
    public ResponseEntity<SocialLoginResponse> kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String accessToken = authUserService.getToken(code, kakaoRequestBodyFactory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(accessToken, kakaoRequestBodyFactory));
    }

    @GetMapping("/user/google/callback")
    public ResponseEntity<SocialLoginResponse> googleLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String accessToken = authUserService.getToken(code, googleRequestBodyFactory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(authUserService.login(accessToken, googleRequestBodyFactory));
    }
}
