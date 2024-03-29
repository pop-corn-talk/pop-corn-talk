package com.popcorntalk.global.exception.customException;

import com.popcorntalk.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PermissionDeniedException extends RuntimeException{
    private ErrorCode errorCode;

    @Override
    public synchronized Throwable fillInStackTrace(){
        return this;
    }
}
