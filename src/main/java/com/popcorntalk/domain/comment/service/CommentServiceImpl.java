package com.popcorntalk.domain.comment.service;

import static com.popcorntalk.global.exception.ErrorCode.COMMENT_NOT_FOUND;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.comment.repository.CommentRepository;
import com.popcorntalk.domain.notification.service.NotificationService;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.service.PostService;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.global.exception.customException.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createComment(User user, Long postId,
        CommentCreateRequestDto requestDto) {

        Post post = postService.getPost(postId);
        Long postUserId = post.getUserId();

        Comment comment = Comment.createOf(requestDto.getContent(), user.getId(), post.getId());

        Comment savedComment = commentRepository.save(comment);

        notificationService.notifyComment(postUserId, user, savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentGetResponseDto> getComments(Long userId, Long postId, Pageable pageable) {
        postService.getPost(postId);
        return commentRepository.findComments(postId, pageable);
    }

    @Override
    @Transactional
    public void updateComment(Long userId, Long postId, Long commentId,
        CommentUpdateRequestDto requestDto) {
        postService.getPost(postId);
        Comment comment = getComment(commentId);
        comment.update(requestDto);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId) {
        postService.getPost(postId);
        Comment comment = getComment(commentId);
        comment.softDelete();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
    }
}
