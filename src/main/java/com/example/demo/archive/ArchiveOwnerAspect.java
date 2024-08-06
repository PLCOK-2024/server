package com.example.demo.archive;

import com.example.demo.common.entity.Archive;
import com.example.demo.common.error.EntityNotFoundException;
import com.example.demo.common.error.ErrorCode;
import com.example.demo.user.domain.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ArchiveOwnerAspect {

    /**
     * 비공개 아카이브 다른사람이 접근했을때 404 반환
     */
    @Around("execution(* com.example.demo.archive.controller.CommentController.*(..))")
    public Object isOwnerIsPrivate(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();

        Archive archive = null;
        User user = null;
        for (Object arg : args) {
            if (arg instanceof Archive a) {
                archive = a;
            } else if (arg instanceof User u) {
                user = u;
            }
        }
        if (archive == null) {
            return joinPoint.proceed();
        }

        if (archive.getIsPublic()) {
            return joinPoint.proceed();
        }

        if (user == null || archive.getAuthor() != user) {
            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND, "is private");
        }

        return joinPoint.proceed();
    }
}