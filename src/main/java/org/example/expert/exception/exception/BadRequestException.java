package org.example.expert.exception.exception;

import org.example.expert.exception.error.ErrorCode;

public class BadRequestException extends RuntimeException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
