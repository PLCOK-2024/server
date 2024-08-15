package com.example.demo.user.controller;

import com.example.demo.common.argumenthandler.Entity;
import com.example.demo.common.service.ReportService;
import com.example.demo.user.service.UserService;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.SignupRequest;
import com.example.demo.user.dto.UserDetailResponse;
import com.example.demo.user.service.relationship.UserBlockService;
import com.example.demo.user.service.relationship.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ReportService reportService;
  
    private final UserBlockService userBlockService;
    private final UserFollowService userFollowService;

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
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userBlockService.link(author, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 차단해제")
    @PostMapping("/{userId}/unblock")
    public ResponseEntity<UserDetailResponse> unblock(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userBlockService.unlink(author, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 팔로우")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<UserDetailResponse> follow(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userFollowService.link(author, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 팔로우 해제")
    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<UserDetailResponse> unfollow(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userFollowService.unlink(author, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userInfo")
    public ResponseEntity<Long> authenticationTest(@AuthenticationPrincipal(errorOnInvalidType = true) User user) {
        return ResponseEntity.status(HttpStatus.OK).body(user.getId());
    }

    @Operation(summary = "신고")
    @ApiResponse(responseCode = "204")
    @PostMapping("{userId}/report")
    public ResponseEntity<?> report(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        reportService.report(author, user);
        return ResponseEntity.noContent().build();
    }
}
