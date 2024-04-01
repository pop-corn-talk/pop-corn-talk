package com.popcorntalk.domain.comment.repository;

import static com.popcorntalk.domain.comment.entity.QComment.comment;
import static com.popcorntalk.domain.user.entity.QUser.user;

import com.popcorntalk.domain.comment.dto.CommentGetResponseDto;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentGetResponseDto> findComments(Long postId, Pageable pageable) {

        List<CommentGetResponseDto> commentList = jpaQueryFactory.
            select(
                Projections.constructor(CommentGetResponseDto.class, comment.id,
                    comment.content, user.email, comment.createdAt, comment.modifiedAt))
            .from(comment)
            .leftJoin(user).on(comment.userId.eq(user.id))
            .where(comment.deletionStatus.eq(DeletionStatus.valueOf("N")),
                comment.postId.eq(postId)
            )
            .orderBy(comment.createdAt.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        if (commentList.isEmpty()) {
            throw new IllegalArgumentException("리스트가 비어있습니다");
        }

        long totalCount = jpaQueryFactory
            .select(comment)
            .from(comment)
            .leftJoin(user).on(comment.userId.eq(user.id))
            .where(comment.deletionStatus.eq(DeletionStatus.valueOf("N")),
                comment.postId.eq(postId))
            .fetch().size();

        return new PageImpl<>(commentList, pageable, totalCount);

    }
}
