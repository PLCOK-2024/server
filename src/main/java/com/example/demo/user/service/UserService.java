package com.example.demo.user.service;

import com.example.demo.common.entity.Block;
import com.example.demo.common.error.BusinessException;
import com.example.demo.common.error.EntityNotFoundException;
import com.example.demo.common.error.ErrorCode;
import com.example.demo.user.domain.RoleType;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.BlockRequest;
import com.example.demo.user.dto.SignupRequest;
import com.example.demo.user.dto.UserDetailResponse;
import com.example.demo.user.repository.BlockRepository;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public Long signUp(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
        User user = request.toEntity();
        user.setRole(RoleType.USER);
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

    /**
     * @param author 차단 한사람
     * @param user 차단 당항 사람
     */
    @Transactional
    public void block(BlockRequest request, User author, User user) {
        if (request.isBlock()) {
            block(author, user);
        } else {
            unblock(author, user);
        }
    }

    private void unblock(User author, User user) {
        if (blockRepository.deleteByAuthorAndBlock(author, user) == 0) {
            throw new BusinessException(ErrorCode.USER_ALREADY_UNBLOCKED);
        }
    }

    private void block(User author, User user) {
        var block = Block.builder()
                .author(author)
                .block(user)
                .build();

        try {
            blockRepository.save(block);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.USER_ALREADY_BLOCKED);
        }
    }
}
