package com.example.demo.user;

import com.example.demo.common.error.BusinessException;
import com.example.demo.common.error.EntityNotFoundException;
import com.example.demo.common.error.ErrorCode;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.SignupRequest;
import com.example.demo.user.dto.UserDetailResponse;
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
}
