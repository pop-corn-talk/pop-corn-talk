package com.popcorntalk.domain.post.service;

import static com.popcorntalk.global.exception.ErrorCode.DELETE_POST_FOUND;
import static com.popcorntalk.global.exception.ErrorCode.PERMISSION_DENIED;
import static com.popcorntalk.global.exception.ErrorCode.POST_NOT_FOUND;

import com.popcorntalk.domain.point.service.PointService;
import com.popcorntalk.domain.post.dto.PostBest3GetResponseDto;
import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.entity.PostEnum;
import com.popcorntalk.domain.post.entity.QPost;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.user.entity.QUser;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import com.popcorntalk.global.exception.customException.PermissionDeniedException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRecodeService postRecodeService;
    private final PointService pointService;
    private final UserService userService;
    private final PostRepository postRepository;

    private final int POST_CREATE_REWORD = 100;
    private final int POST_REWARD = 500;


    @Override
    @Cacheable(value = "Post", key = "#postId", unless = "#result == null", cacheManager = "postCacheManager")
    @Transactional(readOnly = true)
    public PostGetResponseDto getPostById(Long postId) {
        return postRepository.findPost(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PostGetResponseDto> getNormalPosts(Pageable pageable, int type, String keyword) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Predicate deleteNPredicate = QPost.post.deletionStatus.eq(DeletionStatus.N);
        Predicate typePostPredicate = QPost.post.type.eq(PostEnum.POSTED);
        booleanBuilder.and(deleteNPredicate).and(typePostPredicate);

        if (type != 0) {
            switch (type) {
                case 1:
                    Predicate emailEqualPredicate = QUser.user.email.eq(keyword.trim());
                    booleanBuilder.and(emailEqualPredicate);
                    break;
                case 2:
                    if (keyword.trim().isEmpty()) {
                        throw new IllegalArgumentException("검색어를 입력해 주세요");
                    }
                    Predicate titleLikePredicate = QPost.post.name.contains(keyword.trim());
                    booleanBuilder.and(titleLikePredicate);
                    break;
                default:
                    throw new IllegalArgumentException("검색조건이 맞지않습니다.");
            }
        }
        return postRepository.findPosts(pageable, booleanBuilder);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PostGetResponseDto> getNoticePosts(Pageable pageable) {
        Predicate deleteNPredicate = QPost.post.deletionStatus.eq(DeletionStatus.N);
        Predicate typeNoticePredicate = QPost.post.type.eq(PostEnum.NOTICED);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(deleteNPredicate).and(typeNoticePredicate);

        return postRepository.findPosts(pageable, booleanBuilder);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PostGetResponseDto> getDeletePosts(User user, Pageable pageable) {
        userService.validateAdminUser(user.getId());
        Predicate predicate = QPost.post.deletionStatus.eq(DeletionStatus.Y);
        return postRepository.findPosts(pageable, predicate);
    }

    @Override
    @Cacheable(value = "Best3", unless = "#result == null", cacheManager = "best3CacheManager")
    @Transactional(readOnly = true)
    public List<PostBest3GetResponseDto> getBest3PostsInPreMonth() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        LocalDate preMonth = LocalDate.now().minusMonths(1);
        Predicate createAtPredicate = QPost.post.createdAt.between(
            preMonth.withDayOfMonth(1).atStartOfDay(),
            preMonth.withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth())
                .atTime(23, 59, 59, 999999999)
        );
        Predicate deleteNPredicate = QPost.post.deletionStatus.eq(DeletionStatus.N);
        Predicate typePostPredicate = QPost.post.type.eq(PostEnum.POSTED);

        booleanBuilder.and(createAtPredicate).and(deleteNPredicate).and(typePostPredicate);

        List<Long> postIds = postRepository.getBest3PostIds(booleanBuilder);
        return postRepository.getBest3PostsInPreMonth(postIds);
    }

    @Override
    @Transactional
    public void createPost(User user, PostCreateRequestDto requestDto) {
        Post newPost = Post.createOf(requestDto.getPostName(), requestDto.getPostContent(),
            requestDto.getPostImage(), user.getId());

        Post savedPost = postRepository.save(newPost);

        postRecodeService.createPostRecode(user.getId(), savedPost.getId());
        if (postRecodeService.getPostCountInToday(user.getId()) <= 3) {
            pointService.earnPoint(user.getId(), POST_CREATE_REWORD);
        }
    }

    @Override
    @Transactional
    public void createNoticePost(User user, PostCreateRequestDto requestDto) {
        userService.validateAdminUser(user.getId());
        Post noticePost = Post.noticeOf(requestDto.getPostName(), requestDto.getPostContent(),
            requestDto.getPostImage(), user.getId());
        postRepository.save(noticePost);
    }

    @Override
    @CacheEvict(value = "Post", key = "#postId", cacheManager = "postCacheManager")
    @Transactional
    public void updatePost(User user, PostUpdateRequestDto requestDto, Long postId) {
        Post updatePost = getPost(postId);
        validatePostOwner(updatePost.getUserId(), user.getId());

        updatePost.update(requestDto);
    }

    @Override
    @CacheEvict(value = "Post", key = "#postId", cacheManager = "postCacheManager")
    @Transactional
    public void deletePost(User user, Long postId) {
        Post deletePost = getPost(postId);
        validatePostOwner(deletePost.getUserId(), user.getId());

        deletePost.softDelete();
    }

    @Override
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void awardPopularPostsOwners() {
        for (Long userId : getDailyTop3PostsUserIds()) {
            pointService.earnPoint(userId, POST_REWARD);
        }
    }

    @Override
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new NotFoundException(POST_NOT_FOUND));
        if (post.getDeletionStatus().equals(DeletionStatus.Y)) {
            throw new NotFoundException(DELETE_POST_FOUND);
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

    private List<Long> getDailyTop3PostsUserIds() {
        return postRepository.getDailyTop3PostsUserIds();
    }
}
