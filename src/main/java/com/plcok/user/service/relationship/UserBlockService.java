package com.plcok.user.service.relationship;

import com.plcok.user.entity.User;
import com.plcok.user.entity.Block;
import com.plcok.user.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserBlockService implements IUserRelationshipService {
    private final BlockRepository blockRepository;

    @Override
    public void link(User source, User target) {
        var block = Block.builder()
                .author(source)
                .block(target)
                .build();
        blockRepository.save(block);
    }

    @Override
    public int unlink(User source, User target) {
        return blockRepository.deleteByAuthorAndBlock(source, target);
    }
}
