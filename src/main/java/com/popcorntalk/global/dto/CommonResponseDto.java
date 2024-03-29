package com.popcorntalk.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseDto<T> {

    /**
     * status 응답 상태코드 msg 응답메시지 data 응답 데이터
     */
    private String status;
    private String msg;
    private T data;

    public CommonResponseDto(T data) {
        this.data = data;
    }

    public CommonResponseDto(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 성공시 데이터를 전달
     *
     * @param data 데이터
     * @param <T>  응답 데이터 타입
     * @return CommonResponseDto
     */
    public static <T> CommonResponseDto<T> success(T data) {
        return new CommonResponseDto<>(data);
    }

    /**
     * 실패시 상태코드와 메세지만 전달
     *
     * @param status 상태코드
     * @param msg    메세지
     * @return CommonResponseDto
     */
    public static CommonResponseDto fail(String status, String msg) {
        return new CommonResponseDto<>(status, msg, null);
    }

//    /**
//     * 실패시 상태코드와 데이터를 전달
//     *
//     * @param status 상태코드
//     * @param data   데이터
//     * @param <T>    응답 데이터 타입
//     * @return CommonResponseDto
//     */
//    public static <T> CommonResponseDto<T> fail(int status, T data) {
//        return new CommonResponseDto<>(status, null, data);
//    }
}
