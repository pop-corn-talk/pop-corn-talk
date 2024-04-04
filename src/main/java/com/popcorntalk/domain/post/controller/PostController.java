package com.popcorntalk.domain.post.controller;

import com.popcorntalk.domain.post.dto.PostBest3GetResponseDto;
import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostSearchKeywordRequestDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.service.PostService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
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
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    //게시글 단일 조회
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponseDto<PostGetResponseDto>> getPostById(
        @PathVariable Long postId
    ) {
        PostGetResponseDto post = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(post));
    }

    //일반 게시글 전체조회
    @GetMapping
    public ResponseEntity<CommonResponseDto<Slice<PostGetResponseDto>>> getNormalPosts(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        @Valid @RequestBody(required = false) PostSearchKeywordRequestDto requestDto
    ) {
        Slice<PostGetResponseDto> normalPosts = postService.getNormalPosts(pageable, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(normalPosts));
    }

    //공지 게시물 조회
    @GetMapping("/notice")
    public ResponseEntity<CommonResponseDto<Slice<PostGetResponseDto>>> getNoticePosts(
        @PageableDefault(size = 3, sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        Slice<PostGetResponseDto> noticePosts = postService.getNoticePosts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(noticePosts));
    }

    //삭제된 게시물 조회
    @GetMapping("/delete")
    public ResponseEntity<CommonResponseDto<Slice<PostGetResponseDto>>> getDeletePosts(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PageableDefault(sort = "modifiedAt", direction = Direction.DESC) Pageable pageable
    ) {
        Slice<PostGetResponseDto> deletePosts = postService.getDeletePosts(
            userDetails.getUser(),
            pageable
        );
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(deletePosts));
    }

    //전 달 댓글많은 3개의 게시글 조회
    @GetMapping("/best")
    public ResponseEntity<CommonResponseDto<List<PostBest3GetResponseDto>>> getBest3PostsInPreMonth(
    ) {
        List<PostBest3GetResponseDto> best3Posts = postService.getBest3PostsInPreMonth();
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(best3Posts));
    }

    //게시글 등록
    @PostMapping
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody PostCreateRequestDto requestDto
    ) {
        postService.createPost(
            userDetails.getUser(),
            requestDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //관리자 게시물 등록
    @PostMapping("/notice")
    public ResponseEntity<Void> createNoticePost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody PostCreateRequestDto requestDto
    ) {
        postService.createNoticePost(
            userDetails.getUser(),
            requestDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //게시물 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @Valid @RequestBody PostUpdateRequestDto requestDto
    ) {
        postService.updatePost(userDetails.getUser(),
            requestDto,
            postId
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId
    ) {
        postService.deletePost(
            userDetails.getUser(),
            postId
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
