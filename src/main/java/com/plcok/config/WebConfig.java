package com.plcok.config;

import com.plcok.common.argumenthandler.EntityArgumentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final EntityArgumentHandler entityArgumentHandler;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(entityArgumentHandler);
    }
}