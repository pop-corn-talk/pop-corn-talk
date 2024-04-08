package com.popcorntalk.domain.post.repository;

import static com.popcorntalk.global.exception.ErrorCode.SEARCH_POST_NOT_FOUND;

import com.popcorntalk.domain.comment.entity.QComment;
import com.popcorntalk.domain.post.dto.PostBest3GetResponseDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.entity.PostEnum;
import com.popcorntalk.domain.post.entity.QPost;
import com.popcorntalk.domain.user.entity.QUser;
import com.popcorntalk.global.config.QuerydslConfig;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final QuerydslConfig querydslConfig;
    QPost qPost = QPost.post;
    QUser qUser = QUser.user;
    QComment qComment = QComment.comment;

    @Override
    public PostGetResponseDto findPost(Long postId) {
        PostGetResponseDto response = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(PostGetResponseDto.class,
                qPost.name,
                qPost.content,
                qPost.image,
                qUser.email,
                qPost.createdAt,
                qPost.modifiedAt))
            .from(qPost)
            .leftJoin(qUser).on(qPost.userId.eq(qUser.id))
            .where(qPost.deletionStatus.eq(DeletionStatus.valueOf("N")),
                qPost.id.eq(postId))
            .fetchOne();

        if (Objects.isNull(response)) {
            throw new NotFoundException(SEARCH_POST_NOT_FOUND);
        }
        return response;
    }

    @Override
    public Slice<PostGetResponseDto> findPosts(Pageable pageable, Predicate predicate) {
        List<PostGetResponseDto> responses = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(PostGetResponseDto.class,
                qPost.name,
                qPost.content,
                qPost.image,
                qUser.email,
                qPost.createdAt,
                qPost.modifiedAt))
            .from(qPost)
            .leftJoin(qUser).on(qPost.userId.eq(qUser.id))
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(postOrderSpecifier(pageable))
            .fetch();

        if (responses.isEmpty()) {
            throw new NotFoundException(SEARCH_POST_NOT_FOUND);
        }

        boolean hasNext = false;
        if (responses.size() > pageable.getPageSize()) {
            hasNext = true;
            responses.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(responses, pageable, hasNext);
    }

    @Override
    public List<PostBest3GetResponseDto> getBest3PostsInPreMonth(List<Long> postIds) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Predicate postIdPredicate = qPost.id.in(postIds);
        return querydslConfig.jpaQueryFactory()
            .select(Projections.fields(PostBest3GetResponseDto.class,
                qPost.id,
                qPost.name,
                qUser.id.as("userId"),
                qUser.email
            ))
            .from(qPost)
            .leftJoin(qUser).on(qPost.userId.eq(qUser.id))
            .where(postIdPredicate)
            .orderBy()
            .fetch();
    }

    @Override
    public List<Long> getBest3PostIds(Predicate predicate) {
        List<Tuple> postIds = querydslConfig.jpaQueryFactory()
            .select(
                qPost.id,
                qComment.id.count()
            )
            .from(qPost)
            .leftJoin(qComment).on(qPost.id.eq(qComment.postId))
            .where(predicate)
            .groupBy(qPost.id)
            .orderBy(qComment.id.count().desc())
            .limit(3)
            .fetch();

        return postIds.stream().map(i -> i.get(0, Long.class)).toList();
    }

    @Override
    public List<Long> getDailyTop3PostsUserIds() {
        List<Tuple> userIds = querydslConfig.jpaQueryFactory()
            .select(
                qPost.userId,
                qComment.id.count())
            .from(qComment)
            .leftJoin(qPost).on(qComment.postId.eq(qPost.id))
            .where(qPost.createdAt.between(
                    LocalDateTime.now().minusDays(1L).withHour(0).withMinute(0).withSecond(0)
                        .withNano(0),
                    (LocalDateTime.now().minusDays(1L).withHour(23).withMinute(59).withSecond(59)
                        .withNano(999999999)))
                .and(qPost.deletionStatus.eq(DeletionStatus.N))
                .and(qPost.type.eq(PostEnum.POSTED)))
            .groupBy(qPost.userId)
            .orderBy(qComment.id.count().desc())
            .limit(3)
            .fetch();
        return userIds.stream().map(i -> i.get(0, Long.class)).toList();
    }

    public OrderSpecifier<LocalDateTime> postOrderSpecifier(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<Post> postPath = new PathBuilder<>(Post.class, "post");
            DateTimePath<LocalDateTime> dateTimePath;

            switch (order.getProperty()) {
                case "createdAt":
                    dateTimePath = postPath.getDateTime("createdAt", LocalDateTime.class);
                    return new OrderSpecifier<LocalDateTime>(direction, dateTimePath);
                case "modifiedAt":
                    dateTimePath = postPath.getDateTime("modifiedAt", LocalDateTime.class);
                    return new OrderSpecifier<LocalDateTime>(direction, dateTimePath);
                default:
                    throw new IllegalArgumentException("정렬기준을 정확히 선택해주세요");
            }
        }
        return null;
    }
}
