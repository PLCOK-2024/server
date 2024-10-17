package com.plcok.user.controller;

import com.plcok.common.argumenthandler.Entity;
import com.plcok.user.dto.UserCollectResponse;
import com.plcok.user.dto.UserResponse;
import com.plcok.user.dto.UserRetrieveRequest;
import com.plcok.user.dto.response.UserDetailResponse;
import com.plcok.user.entity.User;
import com.plcok.user.dto.request.SignupRequest;
import com.plcok.user.service.UserService;
import com.plcok.user.service.relationship.UserBlockService;
import com.plcok.user.service.relationship.UserFollowService;
import com.plcok.common.service.ReportService;
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
    private final ReportService reportService;
  
    private final UserBlockService userBlockService;
    private final UserFollowService userFollowService;

    @Deprecated
    @Operation(summary = "회원가입")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> signUp(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(request));
    }

    //region 조회
    @Operation(summary = "회원 검색")
    @GetMapping()
    public ResponseEntity<UserCollectResponse> get(
            @AuthenticationPrincipal User user,
            @ModelAttribute UserRetrieveRequest request
    ) {
        return ResponseEntity.ok(userService.get(user, request));
    }

    @Operation(summary = "로그인된 회원 조회")
    @GetMapping("/@me")
    public ResponseEntity<UserDetailResponse> me(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        return ResponseEntity.ok(userService.find(user, user));
    }

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User target
    ) {
        return ResponseEntity.ok(userService.find(user, target));
    }
    //endregion

    //region 차단
    @Operation(summary = "회원 차단")
    @PostMapping("/{userId}/block")
    public ResponseEntity<UserResponse> block(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userBlockService.link(author, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 차단해제")
    @PostMapping("/{userId}/unblock")
    public ResponseEntity<UserResponse> unblock(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userBlockService.unlink(author, user);
        return ResponseEntity.noContent().build();
    }
    //endregion

    //region 팔로우
    @Operation(summary = "회원 팔로우")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<UserResponse> follow(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userFollowService.link(author, user);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 팔로우 해제")
    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<UserResponse> unfollow(
            @PathVariable(name = "userId") long ignoredUserId,
            @Entity(name = "userId") User user,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userFollowService.unlink(author, user);
        return ResponseEntity.noContent().build();
    }
    //endregion

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
