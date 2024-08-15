package com.plcok.user.repository;

import com.plcok.user.entity.User;
import com.plcok.user.entity.Block;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    int deleteByAuthorAndBlock(@Param("author") User author, @Param("block") User block);
}