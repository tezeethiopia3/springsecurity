package com.security.springsecurity.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {
    NO_CODE(0,HttpStatus.NOT_IMPLEMENTED,"No Code"),
    INCORRECT_CURRENT_PASSWORD(300,HttpStatus.BAD_REQUEST,"Current Password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301,HttpStatus.BAD_REQUEST,"The new password does not match"),

    ACCOUNT_LOCKED(302,HttpStatus.FORBIDDEN,"User Account is locked"),
    ACCOUNT_DISABLED(303,HttpStatus.FORBIDDEN,"User Account is disabled"),
    BAD_CREDENTIALS(304,HttpStatus.FORBIDDEN,"password or login is incorrect"),
    DATA_NOY_FOUND(305,HttpStatus.BAD_REQUEST,"record not found"),
    VALIDATION_FAILED(306,HttpStatus.BAD_REQUEST,"validation failed")
    ;

    @Getter
    private final int code;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
