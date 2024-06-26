package com.popcorntalk.domain.post.dto;

import com.popcorntalk.domain.post.entity.PostEnum;
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

    private Long id;
    private String name;
    private String content;
    private String image;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private PostEnum type;
}
