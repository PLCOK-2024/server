package com.example.demo.common.argumenthandler;

import com.example.demo.user.domain.User;
import org.apache.coyote.BadRequestException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class) && parameter.getParameterAnnotation(Auth.class) != null;
    }

    @Override
    public User resolveArgument(
            @Nullable MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            @Nullable NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory
    ) throws BadRequestException {
//        var token = nativeWebRequest.getHeader("Authorization");
        return User.builder()
                .id(1L)
                .build();
    }
}
