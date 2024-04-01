package com.popcorntalk.domain.comment.service;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CommentService {


    void createComment(Long userId, Long postId,
        CommentCreateRequestDto requestDto);

    void updateComment(Long userId, Long postId, Long commentId,
        CommentUpdateRequestDto requestDto);

    void deleteComment(Long userId, Long postId, Long commentId);

    Page<CommentGetResponseDto> getComments(Long userId, Long postId, Pageable pageable);
}
