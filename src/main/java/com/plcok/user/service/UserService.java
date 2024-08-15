package com.plcok.user.service;

import com.plcok.user.entity.User;
import com.plcok.user.dto.request.SignupRequest;
import com.plcok.user.dto.response.UserDetailResponse;
import com.plcok.common.error.BusinessException;
import com.plcok.common.error.EntityNotFoundException;
import com.plcok.common.error.ErrorCode;
import com.plcok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long signUp(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
        User user = request.toEntity();
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND, id+""));

        return UserDetailResponse.from(user);
    }

    public UserDetailResponse find(User user) {
        return UserDetailResponse.from(user);
    }
}
