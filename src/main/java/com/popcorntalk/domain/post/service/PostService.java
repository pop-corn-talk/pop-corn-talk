package com.popcorntalk.domain.post.service;

import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.user.entity.User;

public interface PostService {

    //게시판단일조회
    //게시판 전체조회
    //게시판등록

    /**
     * 게시물 생성
     *
     * @param user       게시물생성자
     * @param requestDto 생성될 게시물의 내용
     */
    void create(User user, PostCreateRequestDto requestDto);
    //게시판수정
    //게시판삭제
    //게시글 이미지 업로드
}
