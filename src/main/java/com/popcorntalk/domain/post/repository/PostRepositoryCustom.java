package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.dto.PostGetResponseDto;

public interface PostRepositoryCustom {

    /**
     * 게시물 단일 조회
     *
     * @param postId 조회할 게시물의 번호
     * @return PostGetResponseDto
     */
    PostGetResponseDto findPost(Long postId);
}
