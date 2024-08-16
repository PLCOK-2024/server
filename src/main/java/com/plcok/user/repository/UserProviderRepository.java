package com.plcok.user.repository;

import com.plcok.user.entity.UserProvider;
import com.plcok.user.entity.enumerated.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {
    Optional<UserProvider> findByProviderTypeAndProviderUserId(ProviderType providerType, String providerUserId);
}
