package com.popcorntalk.domain.comment.service;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommentService {

    /**
     * 댓글 생성
     *
     * @param userId 유저의 존재여부를 확인하기위한 유저의 번호
     * @param postId 게시글의 존재여부를 확인하기 위한 게시글의 번호
     * @param requestDto 생성될 댓글의 내용
     */
    void createComment(Long userId, Long postId,
        CommentCreateRequestDto requestDto);

    /**
     * 댓글 전체조회
     *
     * @param userId 유저의 존재여부를 확인하기위한 유저의 번호
     * @param postId 게시글의 존재여부를 확인하기 위한 게시글의 번호
     * @param pageable 페이징 처리(size=10,page=0)
     * @return Page<CommentGetResponseDto>
     */
    Page<CommentGetResponseDto> getComments(Long userId, Long postId, Pageable pageable);

    /**
     * 댓글 수정
     *
     * @param userId 유저의 존재여부를 확인하기위한 유저의 번호
     * @param postId 게시글의 존재여부를 확인하기 위한 게시글의 번호
     * @param commentId 수정할 댓글의 번호
     * @param requestDto 수정될 댓글의 내용
     */
    void updateComment(Long userId, Long postId, Long commentId,
        CommentUpdateRequestDto requestDto);

    /**
     * 댓글 삭제
     *
     * @param userId 유저의 존재여부를 확인하기위한 유저의 번호
     * @param postId 게시글의 존재여부를 확인하기 위한 게시글의 번호
     * @param commentId 삭제할 댓글의 번호
     */
    void deleteComment(Long userId, Long postId, Long commentId);

    /**
     * 알림 기능에 사용될 댓글
     *
     * @param postId 댓글을 찾을 postId
     * @return 가장 최근에 생성된 댓글 객체 반환
     */
    Comment findLatestComment(Long postId);
}
