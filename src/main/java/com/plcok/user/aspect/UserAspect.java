package com.plcok.user.aspect;

import com.plcok.archive.entity.Archive;
import com.plcok.common.error.EntityNotFoundException;
import com.plcok.common.error.ErrorCode;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserAspect {

    @Around("execution(* com.plcok.user.controller.FolderController.*(..))")
    public Object isOwnerIsPrivate(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var parameters = method.getParameters();

        Folder folder = null;
        User user = null;
        Archive archive = null;
        for (int i = 0; i < args.length; i++) {
            var arg = args[i];
            if (arg instanceof Folder f) {
                folder = f;
            } else if (arg instanceof Archive a) {
                archive = a;
            } else if (arg instanceof User u && parameters[i].getAnnotation(AuthenticationPrincipal.class) != null) {
                user = u;
            }
        }

        if (folder == null) {
            return joinPoint.proceed();
        }

        if (user == null || !folder.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, String.format("folder (%s) is private", folder.getId()));
        }

        if (user == null || !archive.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, String.format("archive (%s) is not mine", archive.getId()));
        }

        return joinPoint.proceed();
    }
}
