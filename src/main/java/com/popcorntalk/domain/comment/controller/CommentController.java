package com.popcorntalk.domain.comment.controller;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.domain.comment.service.CommentService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<Void>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @RequestBody CommentCreateRequestDto requestDto
    ) {
        commentService.createComment(
            userDetails.getUser(),
            postId,
            requestDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<Page<CommentGetResponseDto>>> getComments(

        @PathVariable Long postId,
        @PageableDefault Pageable pageable
    ) {
        Page<CommentGetResponseDto> responseDtoList = commentService.getComments(
            postId,
            pageable
        );
        return ResponseEntity.ok(CommonResponseDto.success(responseDtoList));
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto<Void>> updateComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentUpdateRequestDto requestDto
    ) {
        commentService.updateComment(
            userDetails.getUser().getId(),
            postId,
            commentId,
            requestDto
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @PathVariable Long commentId
    ) {
        commentService.deleteComment(
            userDetails.getUser().getId(),
            postId,
            commentId
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
