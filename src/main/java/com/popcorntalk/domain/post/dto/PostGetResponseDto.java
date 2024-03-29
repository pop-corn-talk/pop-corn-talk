package com.popcorntalk.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostGetResponseDto {

    private String postName;
    private String postContent;
    private String postImage;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
