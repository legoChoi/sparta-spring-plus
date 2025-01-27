package org.example.expert.exception.exception;

import org.example.expert.exception.error.ErrorCode;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
