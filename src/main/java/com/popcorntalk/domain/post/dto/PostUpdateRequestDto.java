package com.popcorntalk.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    @NotEmpty
    private String postName;
    @NotEmpty
    private String postContent;
    @NotEmpty
    private String postImage;

}
