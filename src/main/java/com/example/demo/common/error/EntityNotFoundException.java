package com.example.demo.common.error;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode, String target) {
        super(target + " is not found", errorCode);
    }
}
