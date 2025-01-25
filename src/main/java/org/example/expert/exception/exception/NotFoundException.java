package org.example.expert.exception.exception;

import org.example.expert.exception.error.ErrorCode;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
