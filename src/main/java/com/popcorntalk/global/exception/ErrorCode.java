package com.popcorntalk.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PERMISSION_DENIED("403", "권한이 없습니다.");

    private final String httpStatus;
    private final String msg;

    ErrorCode(String httpStatus, String msg) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
