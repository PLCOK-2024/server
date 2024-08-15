package com.plcok.user.service.relationship;

import com.plcok.common.error.BusinessException;
import com.plcok.common.error.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserRelationshipAspect {
    /**
     * 이미 링크되어 있을때 에러 렌더링
     */
    @Around("execution(* com.plcok.user.service.relationship.IUserRelationshipService.link(..))")
    public Object link(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.USER_ALREADY_BLOCKED);
        }
    }

    /**
     * 링크 안되어 있을떄 있을때 에러 렌더링
     */
    @Around("execution(* com.plcok.user.service.relationship.IUserRelationshipService.unlink(..))")
    public Object unlink(ProceedingJoinPoint joinPoint) throws Throwable {
        var result = (int) joinPoint.proceed();
        if (result == 0) {
            throw new BusinessException(ErrorCode.USER_ALREADY_UNBLOCKED);
        }
        return result;
    }
}