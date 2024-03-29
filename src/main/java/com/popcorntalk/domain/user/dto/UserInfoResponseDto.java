package com.popcorntalk.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoResponseDto {

    private String email;
    private Long point;
    private Long maxDailyPostsLimit;
}
