package com.popcorntalk.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    @NotEmpty
    private String content;
}
