package com.plcok.user.repository;

import com.plcok.user.entity.User;
import com.plcok.user.entity.Follower;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    @Modifying
    @Transactional
    int deleteByFollowerAndFollow(@Param("follower") User follower, @Param("follow") User follow);

    boolean existsByFollowerAndFollow(@Param("follower") User follower, @Param("follow") User follow);
}