package com.popcorntalk.global.exception;

import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.exception.customException.NotFoundException;
import com.popcorntalk.global.exception.customException.PermissionDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "exceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex,
        HttpServletRequest request) {
        log.error("url: {}, 메세지: {}, \n StackTrace", request.getRequestURI(), ex.getMessage(),
            ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail("400", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(IllegalArgumentException ex,
        HttpServletRequest request) {
        log.error("url: {}, 메세지: {}, \n StackTrace", request.getRequestURI(), ex.getMessage(),
            ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail("400", ex.getMessage()));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity handlePermissionDeniedException(PermissionDeniedException ex,
        HttpServletRequest request) {
        log.error("url: {}, 메세지: {}", request.getRequestURI(), ex.getErrorCode().getMsg());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).
            body(CommonResponseDto.fail(ex.getErrorCode().getHttpStatus(),
                ex.getErrorCode().getMsg()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(NotFoundException ex,
        HttpServletRequest request) {
        log.error("url: {}, 메세지: {}", request.getRequestURI(), ex.getErrorCode().getMsg());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
            body(CommonResponseDto.fail(ex.getErrorCode().getHttpStatus(),
                ex.getErrorCode().getMsg()));
    }
}
