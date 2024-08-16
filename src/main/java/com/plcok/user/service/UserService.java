package com.plcok.user.service;

import com.plcok.common.dto.PaginateResponse;
import com.plcok.user.dto.UserCollectResponse;
import com.plcok.user.dto.UserRetrieveRequest;
import com.plcok.user.entity.User;
import com.plcok.user.dto.SignupRequest;
import com.plcok.user.dto.UserResponse;
import com.plcok.common.error.BusinessException;
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

    public UserResponse find(User user, boolean refresh) {
        if (refresh) {
            user = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }
        return find(user);
    }

    public UserResponse find(User user) {
        return UserResponse.from(user);
    }

    public UserCollectResponse get(User user, UserRetrieveRequest request) {
        var users = userRepository.retrieve(user, request);

        var count = users.size();

        count = count >= request.getLimit() ? userRepository.count(user, request) : count;

        return UserCollectResponse.builder()
                .collect(users.stream().map(UserResponse::from).toList())
                .meta(PaginateResponse.builder().count(count).build())
                .build();
    }
}
