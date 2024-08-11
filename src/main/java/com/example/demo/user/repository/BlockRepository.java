package com.example.demo.user.repository;

import com.example.demo.common.entity.Block;
import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Modifying
    @Query("DELETE FROM Block WHERE author = :author AND block = :block")
    int deleteByAuthorAndBlock(@Param("author") User author, @Param("block") User block);
}