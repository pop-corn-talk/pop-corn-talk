package com.popcorntalk.global.exception.customException;

import com.popcorntalk.global.exception.ErrorCode;

public class OutOfDailyPostLimitException extends RuntimeException{
  private ErrorCode errorCode;

  @Override
  public synchronized Throwable fillInStackTrace(){
    return this;
  }
}
