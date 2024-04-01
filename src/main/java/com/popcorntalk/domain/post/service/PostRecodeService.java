package com.popcorntalk.domain.post.service;

public interface PostRecodeService {

    /**
     * Post 생성기록 저장
     *
     * @param userId Post 작성한 유저
     * @param postId 작성된 Post id
     */
    void createPostRecode(Long userId, Long postId);

    /**
     * 하루에 게시글작성으로 받을수있는 포인트를 다 받았는지 확인
     *
     * @param userId 확인할 user id
     * @return true/false
     */
    Boolean isExistsReachedPostLimit(Long userId);
}
