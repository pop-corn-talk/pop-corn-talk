package com.popcorntalk.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //  [EX] new NotFoundException(SECTION_NOT_FOUND)
    //  사용코드      ( HttpStatus        ,    msg              )
    SECTION_NOT_FOUND(400, "Section not exists"),
    DUPLICATE_USER(400, "Section not exists");

    private final int httpStatus;
    private final String msg;

    ErrorCode(int httpStatus, String msg) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
