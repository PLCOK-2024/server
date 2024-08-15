package com.plcok.common.argumenthandler;

import com.plcok.common.extension.NativeWebRequestExtension;
import com.plcok.archive.repository.ArchiveCommentRepository;
import com.plcok.archive.repository.ArchiveRepository;
import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveComment;
import com.plcok.common.error.EntityNotFoundException;
import com.plcok.common.error.ErrorCode;
import com.plcok.user.repository.UserRepository;
import com.plcok.user.entity.User;
import lombok.experimental.ExtensionMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
@ExtensionMethod({NativeWebRequest.class, NativeWebRequestExtension.class})
public class EntityArgumentHandler implements HandlerMethodArgumentResolver {
    private final Map<Type, JpaRepository<?, Long>> repositories = new HashMap<>();

    public EntityArgumentHandler(
            UserRepository userRepository,
            ArchiveRepository archiveRepository,
            ArchiveCommentRepository archiveCommentRepository
    ) {
        repositories.put(User.class, userRepository);
        repositories.put(Archive.class, archiveRepository);
        repositories.put(ArchiveComment.class, archiveCommentRepository);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(Entity.class) != null;
    }

    @Override
    public Object resolveArgument(
            @Nullable MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            @Nullable NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory
    ) {
        if (nativeWebRequest == null || methodParameter == null) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, "");
        }
        var repository = repositories.get(methodParameter.getParameterType());

        var pathParameters = nativeWebRequest.getPathParameters();

        var entityAnnotation = methodParameter.getParameterAnnotation(Entity.class);
        var parameterName = entityAnnotation != null ? entityAnnotation.name() : methodParameter.getParameterName();

        long id;
        try {
            id = Long.parseLong(pathParameters.get(parameterName));
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, "");
        }

        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, ""));
    }
}