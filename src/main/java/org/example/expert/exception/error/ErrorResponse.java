package org.example.expert.exception.error;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        HttpStatus httpStatus,
        String errorMessage
) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getStatus(), errorCode.getMessage());
    }
}
