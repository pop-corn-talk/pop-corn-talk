package com.popcorntalk.domain.post.service;

import static com.popcorntalk.global.exception.ErrorCode.PERMISSION_DENIED;

import com.popcorntalk.domain.point.service.PointService;
import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetImageResponseDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.PermissionDeniedException;
import com.popcorntalk.global.util.StorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@CacheConfig(cacheManager = "postCacheManager")
@Service
public class PostServiceImpl implements PostService {

    private final StorageService storageService;
    private final PostRecodeService postRecodeService;
    private final PointService pointService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final int POST_CREATE_REWORD = 100;


    @Override
    @Cacheable(value = "Post", key = "#postId", unless = "#result == null")
    @Transactional(readOnly = true)
    public PostGetResponseDto getPostById(Long postId) {
        return postRepository.findPost(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PostGetResponseDto> getPosts(Pageable pageable) {
        DeletionStatus deletionStatus = DeletionStatus.N;
        return postRepository.findPosts(pageable, deletionStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PostGetResponseDto> getDeletePosts(User user, Pageable pageable) {
        //1.userRepository 주입
        User adminUser = userRepository.findById(user.getId()).orElseThrow(
            () -> new IllegalArgumentException("해당하는 유저가 없습니다.")
        );
        //2.userService주입
//        user adminUser = userService.findUser(user.getId());
        validateAdminUser(adminUser.getRole());
        DeletionStatus deletionStatus = DeletionStatus.Y;
        return postRepository.findPosts(pageable, deletionStatus);
    }

    @Override
    @Transactional
    public void createPost(User user, PostCreateRequestDto requestDto) {
        Post newPost = Post.createOf(requestDto.getPostName(), requestDto.getPostContent(),
            requestDto.getPostImage(), user.getId());

        Post savedPost = postRepository.save(newPost);

        postRecodeService.createPostRecode(user.getId(), savedPost.getId());
        if (postRecodeService.isExistsReachedPostLimit(user.getId())) {
            pointService.earnPoint(user.getId(), POST_CREATE_REWORD);
        }
    }

    @Override
    @Transactional
    public void createNoticePost(User user, PostCreateRequestDto requestDto) {
        //1.userRepository 주입
        User adminUser = userRepository.findById(user.getId()).orElseThrow(
            () -> new PermissionDeniedException(PERMISSION_DENIED)
        );
        //2.userService주입
//        user adminUser = userService.findUser(user.getId());
        validateAdminUser(adminUser.getRole());
        Post noticePost = Post.noticeOf(requestDto.getPostName(), requestDto.getPostContent(),
            requestDto.getPostImage(), adminUser.getId());
        postRepository.save(noticePost);
    }

    @Override
//    @CachePut(value = "Post", key = "#postId", unless = "#result == null")
//    일단은 업데이트시 캐시를 지우는 방향으로 선회, @CachePut 어노테이션이 정상적으로 작동안됨
    @CacheEvict(value = "Post", key = "#postId")
    @Transactional
    public void updatePost(User user, PostUpdateRequestDto requestDto, Long postId) {
        Post updatePost = getPost(postId);
        validatePostOwner(updatePost.getUserId(), user.getId());

        updatePost.update(requestDto);
    }

    @Override
    @CacheEvict(value = "Post", key = "#postId")
    @Transactional
    public void deletePost(User user, Long postId) {
        Post deletePost = getPost(postId);
        validatePostOwner(deletePost.getUserId(), user.getId());

        deletePost.softDelete();
    }

    @Override
    public PostGetImageResponseDto createImage(MultipartFile file) throws IOException {
        return new PostGetImageResponseDto(getImageUrl(file));
    }

    @Override
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 게시물이 없습니다."));
        if (post.getDeletionStatus().equals(DeletionStatus.Y)) {
            throw new IllegalArgumentException("삭제된 게시물 입니다.");
        }
        return post;
    }

    @Override
    public Boolean isExistsPost(Long postId) {
        return postRepository.existsById(postId);
    }

    private void validatePostOwner(Long postUserId, Long loginUserId) {
        if (!postUserId.equals(loginUserId)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }
    }

    private void validateAdminUser(UserRoleEnum role) {
        if (!role.equals(UserRoleEnum.ADMIN)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }
    }

    private String getImageUrl(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }
        return storageService.uploadFile(file);
    }
}
