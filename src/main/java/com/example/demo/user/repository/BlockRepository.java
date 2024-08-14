package com.example.demo.user.repository;

import com.example.demo.common.entity.Block;
import com.example.demo.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    int deleteByAuthorAndBlock(@Param("author") User author, @Param("block") User block);
}