package com.popcorntalk.domain.post.dto;

import lombok.Getter;

@Getter
public class PostCreateRequestDto {

    private String postName;
    private String postContent;
    private String postImage;
}
