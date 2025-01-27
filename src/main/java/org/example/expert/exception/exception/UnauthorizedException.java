package org.example.expert.exception.exception;

import org.example.expert.exception.error.ErrorCode;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
