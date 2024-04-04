package com.popcorntalk.domain.post.service;

import com.popcorntalk.domain.post.dto.PostBest3GetResponseDto;
import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostSearchKeywordRequestDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostService {

    /**
     * 게시물 단일 조회
     *
     * @param postId 조회할 게시물의 번호
     * @return PostGetResponseDto
     */
    PostGetResponseDto getPostById(Long postId);

    /**
     * 모든 일반 게시물 조회
     *
     * @param pageable   페이징처리(기본값: size 10, page 0, order createdAt::DESC)
     * @param requestDto 검색 타입,키워드
     * @return List<PostGetResponseDto>
     */
    Slice<PostGetResponseDto> getNormalPosts(Pageable pageable,
        PostSearchKeywordRequestDto requestDto);

    /**
     * 모든 공지 게시물 조회
     *
     * @param pageable 페이징처리(기본값: size 10, page 0, order createdAt::DESC)
     * @return Slice<PostGetResponseDto>
     */
    Slice<PostGetResponseDto> getNoticePosts(Pageable pageable);

    /**
     * 삭제된 모든 게시물 조회
     *
     * @param user     로그인유저
     * @param pageable 페이징처리(기본값: size 3, page 0, order modifiedAt::DESC)
     * @return Slice<PostGetResponseDto>
     */
    Slice<PostGetResponseDto> getDeletePosts(User user, Pageable pageable);

    /**
     * 전 달에 가장 인기있었던(댓글↑) 게시물 3개 조회
     *
     * @return List<PostBest3GetResponseDto>
     */
    List<PostBest3GetResponseDto> getBest3PostsInPreMonth();

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

    /**
     * [Cron 동작] 매일 9시에 전날 댓글이 가장 많은 게시물작성자에게 포인트 지급
     */
    void awardPopularPostsOwners();

    /**
     * Post Entity 조회
     *
     * @param postId 조회할 Post Entity id
     * @return Post
     */
    Post getPost(Long postId);

    /**
     * Post Entity 존재 확인
     *
     * @param postId 확인할 Post Entity od
     * @return true/false
     */
    Boolean isExistsPost(Long postId);
}
