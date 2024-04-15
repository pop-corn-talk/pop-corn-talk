package com.popcorntalk.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    private String email;
    private Integer point;
    private Integer DailyPostsLimit;
}
