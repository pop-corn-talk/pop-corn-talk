package com.popcorntalk.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostBest3GetResponseDto {

    private Long id;
    private String name;
    private Long userId;
    private String email;
}
