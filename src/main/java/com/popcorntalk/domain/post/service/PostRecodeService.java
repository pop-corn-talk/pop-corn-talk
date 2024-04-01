package com.popcorntalk.domain.post.service;

public interface PostRecodeService {

    /**
     * Post 생성기록 저장
     *
     * @param userId Post 작성한 유저
     * @param postId 작성된 Post id
     */
    void createPostRecode(Long userId, Long postId);
}
