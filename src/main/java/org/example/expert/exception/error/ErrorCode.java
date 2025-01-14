package org.example.expert.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    USER_EMAIL_NOT_FOUND(HttpStatus.CONFLICT, "해당 이메일로 가입된 유저가 없습니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    USER_DELETED(HttpStatus.GONE, "탈퇴한 유저입니다."),
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_PASSWORD(HttpStatus.CONFLICT, "이미 사용 중인 비밀번호입니다."),

    // 메뉴, 주문 관련 익셉션
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),
    MENU_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이 메뉴는 이미 삭제되었습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    ORDER_NOT_CHANGE(HttpStatus.NOT_FOUND, "주문 상태를 변경할 수 없습니다."),

    // 가게 관련 익셉션
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    STORE_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 폐업 처리된 가게입니다."),
    STORE_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "가게는 최대 3개까지만 운영할 수 있습니다"),
    INVALID_MIN_ORDER_PRICE_UNIT(HttpStatus.BAD_REQUEST, "최소 주문 금액은 천원 단위만 입력 가능합니다,"),

    // 리뷰 관련 익셉션
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "배달 완료 상태가 아닙니다."),
    INVALID_STAR_RANGE(HttpStatus.BAD_REQUEST, "별점 범위가 유효하지 않습니다."),
    STAR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 별점의 리뷰가 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "이미 리뷰가 작성된 주문입니다."),

    // 즐겨찾기 관련 익셉션
    DUPLICATED_FAVORITE(HttpStatus.CONFLICT, "이미 즐겨찾기한 가게입니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "즐겨찾기가 존재하지 않습니다."),
    SELF_FAVORITE_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "자기 자신의 가게를 즐겨찾기할 수 없습니다."),
    SELF_ADD_REVIEW_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "자기 자신의 가게에 리뷰를 등록할 수 없습니다."),

    // 댓글 관련 익셉션
    DUPLICATED_REPLY(HttpStatus.CONFLICT, "이미 댓글이 작성된 리뷰입니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),

    // Security 관련 익셉션
    LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    NEED_LOGIN_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTHORIZATION_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // JWT 관련 익셉션
    JWT_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "jwt token error"),

    // 장바구니 관련 익셉션
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저의 장바구니가 존재하지 않습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 해당 메뉴가 존재하지 않습니다."),
    INVALID_DECREASE_QUANTITY(HttpStatus.BAD_REQUEST, "수량이 0보다 적을 순 없습니다.");

    private final HttpStatus status;
    private final String message;
}
