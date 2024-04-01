package com.popcorntalk.domain.post.service;

import static com.popcorntalk.global.exception.ErrorCode.PERMISSION_DENIED;

import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.PermissionDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public PostGetResponseDto getPost(Long postId) {
        return postRepository.findPost(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostGetResponseDto> getPosts(Pageable pageable) {
        return postRepository.findPosts(pageable);
    }

    @Override
    @Transactional
    public void createPost(User user, PostCreateRequestDto requestDto) {
        Post newPost = Post.toEntity(requestDto, user.getId());
        postRepository.save(newPost);
    }


    @Override
    @Transactional
    public void updatePost(User user, PostUpdateRequestDto requestDto, Long postId) {
        Post updatePost = findPost(postId);
        validatePostOwner(updatePost.getUserId(), user.getId());

        updatePost.update(requestDto);
    }

    @Override
    @Transactional
    public void deletePost(User user, Long postId) {
        Post deletePost = findPost(postId);
        validatePostOwner(deletePost.getUserId(), user.getId());

        deletePost.softDelete();
    }

    public Post findPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 게시물이 없습니다."));
        if (post.getDeletionStatus().equals(DeletionStatus.Y)) {
            throw new IllegalArgumentException("삭제된 게시물 입니다.");
        }
        return post;
    }

    private void validatePostOwner(Long postUserId, Long loginUserId) {
        if (!postUserId.equals(loginUserId)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }
    }
}
