package com.plcok.user.service;

import com.plcok.common.error.EntityNotFoundException;
import com.plcok.common.error.ErrorCode;
import com.plcok.common.security.JwtUtil;
import com.plcok.user.entity.User;
import com.plcok.user.dto.LoginRequest;
import com.plcok.user.dto.TokenResponse;
import com.plcok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestAuthServiceImpl implements TestAuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public TokenResponse testLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND, request.getEmail() + ""));
        return TokenResponse.builder()
                .accessToken(jwtUtil.createToken(user.getId(), user.getRole(), user.getCreatedAt()))
                .build();
    }
}
