package com.example.demo.user;

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
        User user = request.toEntity();
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        return UserDetailResponse.from(user);
    }
}
