package com.popcorntalk.domain.post.service;

import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostService {

    /**
     * 게시물 단일 조회
     *
     * @param postId 조회할 게시물의 번호
     * @return PostGetResponseDto
     */
    PostGetResponseDto getPost(Long postId);

    /**
     * 모든 게시물 조회
     *
     * @param pageable 페이징처리(기본값: size 10, page 0, order createdAt::DESC)
     * @return List<PostGetResponseDto>
     */
    Slice<PostGetResponseDto> getPosts(Pageable pageable);

    /**
     * 게시물 생성
     *
     * @param user       로그인유저(게시물생성자)
     * @param requestDto 생성될 게시물의 내용
     */
    void createPost(User user, PostCreateRequestDto requestDto);

    /**
     * 공지 게시물 작성
     *
     * @param user       로그인 유저(게시물생성자)
     * @param requestDto 생성될 게시물의 내용
     */
    void createNoticePost(User user, PostCreateRequestDto requestDto);

    /**
     * 게시물 수정
     *
     * @param user       로그인 유저
     * @param requestDto 수정할 게시물의 내용
     * @param postId     수정할 게시물의 번호
     */
    void updatePost(User user, PostUpdateRequestDto requestDto, Long postId);

    /**
     * 게시물 삭제
     *
     * @param user   로그인 유저
     * @param postId 삭제할 게시물의 번호
     */
    void deletePost(User user, Long postId);
}
