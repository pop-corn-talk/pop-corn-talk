package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.dto.PostBest3GetResponseDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
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

    /**
     * 전 달에 가장 인기있었던(댓글↑) 게시물 조회
     *
     * @param postIds 조회할 게시물 id
     * @return List<PostBest3GetResponseDto>
     */
    List<PostBest3GetResponseDto> getBest3PostsInPreMonth(List<Long> postIds);

    /**
     * 저번달 게시물 중 댓글이 가장 많은 게시글 id 3개 조회
     *
     * @param predicate 검색조건
     * @return List<Long>
     */
    List<Long> getBest3PostIds(Predicate predicate);
}
