package com.example.demo.user.repository;

import com.example.demo.user.domain.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {
    Optional<UserProvider> findByProviderTypeAndProviderUserId(String providerType, String providerUserId);
}
