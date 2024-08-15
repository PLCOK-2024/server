package com.example.demo.user.service.relationship;

import com.example.demo.common.entity.Follower;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFollowService implements IUserRelationshipService {
    private final FollowerRepository followerRepository;

    @Override
    public void link(User source, User target) {
        var block = Follower.builder()
                .follower(source)
                .follow(target)
                .build();
        followerRepository.save(block);
    }

    @Override
    public int unlink(User source, User target) {
        return followerRepository.deleteByFollowerAndFollow(source, target);
    }
}
