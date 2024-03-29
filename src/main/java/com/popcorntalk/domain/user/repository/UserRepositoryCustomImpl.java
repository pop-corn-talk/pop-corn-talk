package com.popcorntalk.domain.user.repository;

import com.popcorntalk.domain.user.dto.UserPublicInfoResponseDto;
import com.popcorntalk.domain.user.entity.QUser;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  private final QUser user = QUser.user;

  @Override
  public User getUser(Long userId) {
    BooleanExpression predicate = user.id.eq(userId)
        .and(user.deletionStatus.eq(DeletionStatus.N));

    return jpaQueryFactory.selectFrom(user).where(predicate).fetchOne();
  }
  @Override
  public Page<UserPublicInfoResponseDto> getPageUsers(Pageable pageable) {

    BooleanExpression predicate = user.deletionStatus.eq(DeletionStatus.N);

    List<UserPublicInfoResponseDto> userPublicDto =
        jpaQueryFactory
        .select(Projections.fields(UserPublicInfoResponseDto.class, user.email))
        .from(user)
        .where(predicate)
         .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(userPublicDto,pageable,userPublicDto.size());
}
}