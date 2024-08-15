package com.example.demo.user.repository;

import com.example.demo.common.entity.Follower;
import com.example.demo.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    @Modifying
    @Transactional
    int deleteByFollowerAndFollow(@Param("follower") User follower, @Param("follow") User follow);
}