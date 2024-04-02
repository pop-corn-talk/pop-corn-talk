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
     * @return user가 오늘 작성한 게시글 수
     */
    int getPostCountInToday(Long userId);

    /**
     * Cron을 이용한 7일 전 작성기록 삭제 설정 PostRecode 도메인의 역할 : 게시글작성했을때 하루 3개 작성까지 포인트지급 체크
     */
    void deletePostRecode();
}
