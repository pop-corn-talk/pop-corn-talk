package com.popcorntalk.domain.comment.controller;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.domain.comment.service.CommentService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponseDto<Void>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId,
        @RequestBody CommentCreateRequestDto requestDto) {

        commentService.createComment(userDetails, postId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponseDto<Void>> updateComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId, @PathVariable Long commentId,
        @RequestBody CommentUpdateRequestDto requestDto) {
        commentService.updateComment(userDetails, postId, commentId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
