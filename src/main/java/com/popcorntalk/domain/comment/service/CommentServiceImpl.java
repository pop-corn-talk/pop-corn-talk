package com.popcorntalk.domain.comment.service;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.comment.repository.CommentRepository;
import com.popcorntalk.domain.post.service.PostServiceImpl;
import com.popcorntalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostServiceImpl postServiceImpl;
    private final UserRepository userRepository;

    @Override
    public void createComment(Long userId, Long postId,
        CommentCreateRequestDto requestDto) {
        isExistsUser(userId);
        postServiceImpl.findPost(postId);
        Comment comment = Comment.createOf(requestDto, userId, postId);
        commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentGetResponseDto> getComments(Long userId, Long postId, Pageable pageable) {
        isExistsUser(userId);
        postServiceImpl.findPost(postId);
        return commentRepository.findComments(postId, pageable);
    }

    @Override
    public void updateComment(Long userId, Long postId, Long commentId,
        CommentUpdateRequestDto requestDto) {

        isExistsUser(userId);
        postServiceImpl.findPost(postId);

        Comment comment = findComment(commentId);
        comment.update(requestDto);
    }

    @Override
    public void deleteComment(Long userId, Long postId, Long commentId) {

        isExistsUser(userId);
        postServiceImpl.findPost(postId);
        Comment comment = findComment(commentId);
        comment.softDelete();
        // 영속성 컨테이너 : 처음에 준 정보(1차캐시)와 나중에 준 정보의 내용이 다르면 update시켜줌.
    }

    private void isExistsUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다");
        }
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 작성한 댓글이 존재하지 않습니다"));
    }
}
