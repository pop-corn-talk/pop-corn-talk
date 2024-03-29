package com.popcorntalk.domain.post.controller;

import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.service.PostService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    //게시판단일조회
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<PostGetResponseDto>> getPost(
        @PathVariable Long postId) {

        return ResponseEntity.status(HttpStatus.OK).
            body(CommonResponseDto.success(postService.getPost(postId)));
    }

    //게시판 전체조회
    //게시판등록
    @PostMapping
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody PostCreateRequestDto requestDto) {
        postService.createPost(userDetails.getUser(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //게시판수정
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @Valid @RequestBody PostUpdateRequestDto requestDto) {
        postService.updatePost(userDetails.getUser(), requestDto, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //게시판삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId) {
        postService.deletePost(userDetails.getUser(), postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    //게시글 이미지 업로드
}
