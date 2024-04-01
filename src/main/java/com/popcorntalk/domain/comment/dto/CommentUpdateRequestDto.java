package com.popcorntalk.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {

    @NotEmpty
    private String content;
}
