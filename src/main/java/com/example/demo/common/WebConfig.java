package com.example.demo.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import com.example.demo.common.argumenthandler.EntityArgumentHandler;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebConfig extends WebMvcConfigurationSupport {
    private final EntityArgumentHandler entityArgumentHandler;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(entityArgumentHandler);
    }
}
