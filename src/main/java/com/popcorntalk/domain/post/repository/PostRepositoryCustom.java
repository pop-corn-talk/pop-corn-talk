package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {

    /**
     * 게시물 단일 조회
     *
     * @param postId 조회할 게시물의 번호
     * @return PostGetResponseDto
     */
    PostGetResponseDto findPost(Long postId);

    /**
     * 게시물 전체 조회
     *
     * @param pageable  페이징처리
     * @param predicate 쿼리 조회 조건
     * @return Slice<PostGetResponseDto>
     */
    Slice<PostGetResponseDto> findPosts(Pageable pageable, Predicate predicate);
}
