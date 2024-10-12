package com.plcok.user.service;

import com.plcok.common.dto.PaginateResponse;
import com.plcok.user.dto.UserCollectResponse;
import com.plcok.user.dto.UserResponse;
import com.plcok.user.dto.UserRetrieveRequest;
import com.plcok.user.dto.response.UserDetailResponse;
import com.plcok.user.entity.User;
import com.plcok.user.dto.request.SignupRequest;
import com.plcok.common.error.BusinessException;
import com.plcok.common.error.ErrorCode;
import com.plcok.user.repository.FollowerRepository;
import com.plcok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Transactional
    public Long signUp(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
        User user = request.toEntity();
        return userRepository.save(user).getId();
    }

    public UserDetailResponse find(User user, User target) {
        target = userRepository.findById(target.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        boolean isFollow = followerRepository.existsByFollowerAndFollow(user, target);
        return UserDetailResponse.fromEntity(target, isFollow);
    }

    public UserCollectResponse get(User user, UserRetrieveRequest request) {
        var users = userRepository.retrieve(user, request);

        var count = users.size();

        count = count >= request.getLimit() ? userRepository.count(user, request) : count;

        return UserCollectResponse.builder()
                .collect(users.stream().map(UserResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(count).build())
                .build();
    }
}
