package com.popcorntalk.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PERMISSION_DENIED("403", "권한이 없습니다."),
    NOT_FOUND("404", "찾을 수 없습니다."),
    POINT_NOT_FOUND("404", "포인트가 존재하지 않습니다."),
    INSUFFICIENT_POINT("400", "포인트가 부족합니다"),
    DUPLICATE_USER("400", "중복된 이메일 입니다."),
    USER_NOT_FOUND("400", "해당 유저를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("400","해당 댓글을 찾을 수 없습니다.");
    private final String httpStatus;
    private final String msg;

    ErrorCode(String httpStatus, String msg) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
