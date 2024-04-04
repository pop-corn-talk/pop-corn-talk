package com.popcorntalk.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearchKeywordRequestDto {

    /**
     * type: [1:e-mail], [2:title]
     */
    @PositiveOrZero
    private int type;
    @NotEmpty
    private String keyword;
}
