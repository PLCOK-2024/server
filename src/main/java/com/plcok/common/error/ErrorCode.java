package com.plcok.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_TYPE_VALUE(400, "C001", "Invalid type value"),
    INVALID_INPUT_VALUE(400, "C003", "Invalid input value"),
    METHOD_NOT_ALLOWED(405, "C004", "Method not allowed"),
    ACCESS_DENIED(403, "C005", "Access denied"),
    INTERNAL_SERVER_ERROR(500, "C006", "Internal server error"),
    ENTITY_NOT_FOUND(404, "C007", "Not Found"),


    // User
    EMAIL_DUPLICATION(400, "U001", "Email is duplication"),
    USER_NOT_FOUND(404, "U002", "User not found"),

    // 차단
    USER_ALREADY_BLOCKED(400, "B001", "Already Blocked user"),
    USER_ALREADY_UNBLOCKED(400, "U002", "Already unblocked user"),

    // Auth
    UNAUTHORIZED(401, "T001", "UNAUTHORIZED"),


    // OAuth
    PROVIDER_NOT_VALID(404, "O001", "this provider is not valid");


    private final int status;
    private final String code;
    private final String message;
}
