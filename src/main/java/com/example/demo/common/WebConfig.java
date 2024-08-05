package com.example.demo.common;

import com.example.demo.common.argumenthandler.AuthArgumentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import com.example.demo.common.argumenthandler.EntityArgumentHandler;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {
    private final EntityArgumentHandler entityArgumentHandler;
    private final AuthArgumentHandler userArgumentHandler;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(entityArgumentHandler);
        argumentResolvers.add(userArgumentHandler);
    }
}
