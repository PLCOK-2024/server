package com.plcok.notification.controller;

import com.plcok.notification.dto.request.NotificationTokenRequest;
import com.plcok.notification.service.NotificationService;
import com.plcok.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "디바이스 토큰 정보 저장")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<Void> saveDeviceToken(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
                                                @RequestBody NotificationTokenRequest request) {
        notificationService.saveDeviceToken(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
