// todo 퀘리 dsl query dsl 마춰서 복원 해야 합니다.
//package com.popcorntalk.domain.user.repository;
//
//import com.popcorntalk.domain.user.entity.QUser;
//import com.popcorntalk.domain.user.entity.User;
//import com.popcorntalk.global.entity.DeletionStatus;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class UserRepositoryCustomImpl implements UserRepositoryCustom {
//
//  private final JPAQueryFactory jpaQueryFactory;
//  private final QUser user = QUser.user;
//
//  @Override
//  public User getUser(Long userId) {
//    BooleanExpression predicate = user.id.eq(userId)
//        .and(user.deletionStatus.eq(DeletionStatus.N));
//
//    return jpaQueryFactory.selectFrom(user).where(predicate).fetchOne();
//  }
//}
