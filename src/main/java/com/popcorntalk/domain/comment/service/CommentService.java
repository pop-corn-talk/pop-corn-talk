package com.popcorntalk.domain.comment.service;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CommentService {


    void createComment(UserDetailsImpl userDetails, Long postId,
        CommentCreateRequestDto requestDto);

    void updateComment(UserDetailsImpl userDetails, Long postId, Long commentId,
        CommentUpdateRequestDto requestDto);

    void deleteComment(UserDetailsImpl userDetails, Long postId, Long commentId);


}
