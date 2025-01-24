package org.example.expert.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    USER_EMAIL_NOT_FOUND(HttpStatus.CONFLICT, "해당 이메일로 가입된 유저가 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),

    // Security 관련 익셉션
    LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    NEED_LOGIN_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTHORIZATION_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // JWT 관련 익셉션
    INVALID_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    REFRESH_TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
