package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.entity.QPost;
import com.popcorntalk.domain.user.entity.QUser;
import com.popcorntalk.global.config.QuerydslConfig;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

    @Override
    public PostGetResponseDto findPost(Long postId) {
        PostGetResponseDto response = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(PostGetResponseDto.class,
                qPost.postName,
                qPost.postContent,
                qPost.postImage,
                qUser.email,
                qPost.createdAt,
                qPost.modifiedAt))
            .from(qPost)
            .leftJoin(qUser).on(qPost.userId.eq(qUser.id))
            .where(qPost.deletionStatus.eq(DeletionStatus.valueOf("N")),
                qPost.id.eq(postId))
            .fetchOne();

        if (Objects.isNull(response)) {
            throw new IllegalArgumentException("해당하는 게시물이 없습니다.");
        }
        return response;
    }

    @Override
    public Slice<PostGetResponseDto> findPosts(Pageable pageable) {
        List<PostGetResponseDto> responses = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(PostGetResponseDto.class,
                qPost.postName,
                qPost.postContent,
                qPost.postImage,
                qUser.email,
                qPost.createdAt,
                qPost.modifiedAt))
            .from(qPost)
            .leftJoin(qUser).on(qPost.userId.eq(qUser.id))
            .where(qPost.deletionStatus.eq(DeletionStatus.valueOf("N")))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(postOrderSpecifier(pageable))
            .fetch();

        if (Objects.isNull(responses)) {
            throw new IllegalArgumentException("게시물이 없습니다.");
        }

        boolean hasNext = false;
        if (responses.size() > pageable.getPageSize()) {
            hasNext = true;
            responses.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(responses, pageable, hasNext);
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
