package com.popcorntalk.domain.post.controller;

import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.service.PostService;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    //게시판단일조회
    //게시판 전체조회
    //게시판등록
    @PostMapping
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PostCreateRequestDto requestDto) {
        postService.createPost(userDetails.getUser(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //게시판수정
    //게시판삭제
    //게시글 이미지 업로드
}
