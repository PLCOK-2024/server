package com.example.demo.user.controller;

import com.example.demo.archive.dto.CreateCommentRequest;
import com.example.demo.common.argumenthandler.Entity;
import com.example.demo.user.dto.BlockRequest;
import com.example.demo.user.service.UserService;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.SignupRequest;
import com.example.demo.user.dto.UserDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "회원 API")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> signUp(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(request));
    }

    @Operation(summary = "로그인된 회원 조회")
    @GetMapping("/@me")
    public ResponseEntity<UserDetailResponse> getLoginUserDetail(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        return ResponseEntity.ok(userService.find(user));
    }

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user
    ) {
        return ResponseEntity.ok(userService.find(user));
    }

    @Operation(summary = "회원 차단")
    @PostMapping("/{userId}/block")
    public ResponseEntity<UserDetailResponse> block(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author,
            @RequestBody BlockRequest request
    ) {
        userService.block(request, author, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userInfo")
    public ResponseEntity<String> authenticationTest(@AuthenticationPrincipal(errorOnInvalidType = true) User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(user.getEmail());
    }
}
