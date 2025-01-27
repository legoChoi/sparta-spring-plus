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
    SAME_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    INVALID_PASSWORD_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호 입니다."),
    WRONG_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST, "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),

    // Security 관련 익셉션
    LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    NEED_LOGIN_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTHORIZATION_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // JWT 관련 익셉션
    INVALID_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),

    // Manager 관련 익셉션
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "관리 매니저가 존재하지 않습니다."),
    MANAGER_SELF_ASSIGNMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "일정 작성자는 본인을 담당자로 등록할 수 없습니다."),
    NOT_ASSIGNED_MANAGER_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 일정에 등록된 담당자가 아닙니다."),

    // Todo 관련 익셉션
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "투두 게시글이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
