package com.example.demo.common.oauth;


import com.example.demo.common.error.BusinessException;
import com.example.demo.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuthRequestBodyFactoryProvider {

    private final Map<String, OAuthRequestBodyFactory> factoryMap;

    public OAuthRequestBodyFactory getFactory(String providerType) {
        return Optional.ofNullable(factoryMap.get(providerType))
                .orElseThrow(() -> new BusinessException(ErrorCode.PROVIDER_NOT_VALID));
    }
}
