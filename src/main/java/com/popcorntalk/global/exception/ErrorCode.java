package com.popcorntalk.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //  [EX] new NotFoundException(SECTION_NOT_FOUND)
    //  사용코드      ( HttpStatus        ,    msg              )
    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Section not exists");

    private final HttpStatus httpStatus;
    private final String msg;

    ErrorCode(HttpStatus httpStatus, String msg){
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
