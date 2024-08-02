package com.example.demo.user;

import com.example.demo.user.dto.SignupRequest;
import com.example.demo.user.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> signUp(@RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserDetail(userId));
    }
}
