package com.popcorntalk.domain.comment.service;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.comment.repository.CommentRepository;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void createComment(UserDetailsImpl userDetails, Long postId,

        CommentCreateRequestDto requestDto) {

        validateUser(userDetails);
        validatePost(postId);

        Comment comment = Comment.createOf(requestDto, userDetails.getUser().getId(), postId);
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(UserDetailsImpl userDetails, Long postId, Long commentId,
        CommentUpdateRequestDto requestDto) {

        validateUser(userDetails);
        validatePost(postId);

        Comment comment = getCommentOrElseThrow(commentId);
        comment.softUpdate(requestDto);
    }

    @Override
    public void deleteComment(UserDetailsImpl userDetails, Long postId, Long commentId) {

        validateUser(userDetails);
        validatePost(postId);
        validateComment(commentId);
        Comment comment = getCommentOrElseThrow(commentId);
        comment.softDelete();
        // 영속성 컨테이너 : 처음에 준 정보(1차캐시)와 나중에 준 정보의 내용이 다르면 update시켜줌.
    }

    private void validateUser(UserDetailsImpl userDetails) {
        if (!userRepository.existsById(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다");
        }
    }

    private void validatePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다");
        }
    }

    private void validateComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("해당 게시글에 작성한 댓글이 존재하지 않습니다");
        }
    }

    private Comment getCommentOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 작성한 댓글이 존재하지 않습니다"));
    }
}
