package org.example.expert.config;

import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.exception.exception.BadRequestException;
import org.example.expert.exception.exception.ForbiddenException;
import org.example.expert.exception.exception.NotFoundException;
import org.example.expert.exception.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> invalidRequestExceptionException(InvalidRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> badRequestExceptionException(BadRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> unauthorizedExceptionException(UnauthorizedException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> forbiddenExceptionException(ForbiddenException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFoundExceptionException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Map<String, Object>> handleServerException(ServerException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return getErrorResponse(status, ex.getMessage());
    }

    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}

