package com.popcorntalk.domain.comment.service;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.entity.Comment;
import com.popcorntalk.domain.comment.repository.CommentRepository;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public void createComment(UserDetailsImpl userDetails, Long postId, CommentCreateRequestDto requestDto) {

        if (postRepository.findById(postId).isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다");
        }
        if (userRepository.findById(userDetails.getUser().getId()).isEmpty()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다");
        }

        Comment comment = Comment.of(requestDto,userDetails.getUser().getId(), postId);
        commentRepository.save(comment);
    }
}
