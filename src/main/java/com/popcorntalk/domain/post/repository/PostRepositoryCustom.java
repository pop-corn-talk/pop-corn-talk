package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

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
     * @param pageable 페이징처리(기본값: size 10, page 0, order createdAt::DESC)
     * @return List<PostGetResponseDto>
     */
    List<PostGetResponseDto> findPosts(Pageable pageable);
}
