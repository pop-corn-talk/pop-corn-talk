package com.popcorntalk.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentGetResponseDto {

    private Long id;
    private String content;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
