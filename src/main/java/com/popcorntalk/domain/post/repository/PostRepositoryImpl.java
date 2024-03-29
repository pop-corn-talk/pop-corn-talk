package com.popcorntalk.domain.post.repository;

import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.entity.QPost;
import com.popcorntalk.domain.user.entity.QUser;
import com.popcorntalk.global.config.QuerydslConfig;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Projections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;


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
}
