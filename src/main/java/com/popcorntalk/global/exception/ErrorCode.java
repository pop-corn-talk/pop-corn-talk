package com.popcorntalk.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //  [EX] new NotFoundException(SECTION_NOT_FOUND)
    //  사용코드      ( HttpStatus        ,    msg              )
    PERMISSION_DENIED(403, "권한이 없습니다.");

    private final int httpStatus;
    private final String msg;

    ErrorCode(int httpStatus, String msg){
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
