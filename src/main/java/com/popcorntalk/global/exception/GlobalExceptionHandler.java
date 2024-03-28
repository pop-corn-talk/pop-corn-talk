package com.popcorntalk.global.exception;

import com.popcorntalk.global.dto.CommonResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "exceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("url: {}, 메세지: {}, 에러코드: {}, \n StachTrace: {}",request.getRequestURI(),ex.getMessage(),ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail(400, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(IllegalArgumentException ex, HttpServletRequest request) {
        log.error("url: {}, 메세지: {} \n stacktrace: {}",request.getRequestURI(),ex.getMessage(), ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail(400, ex.getMessage()));
    }
}
