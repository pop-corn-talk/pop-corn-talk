package com.popcorntalk.domain.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateRequestDto {

    @NotEmpty
    private String postName;
    @NotEmpty
    private String postContent;
    @NotEmpty
    private String postImage;

}
