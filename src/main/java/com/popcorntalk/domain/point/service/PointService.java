package com.popcorntalk.domain.point.service;

import com.popcorntalk.domain.point.entity.Point;

public interface PointService {

    /**
     * 상품 구매시 포인트 차감 메서드
     *
     * @param userId         포인트 차감할 유저 Id
     * @param purchaseAmount 상품 금액
     */
    void deductPointForPurchase(Long userId, int purchaseAmount);

    /**
     * 회원가입 시 최초 포인트 지급 메서드
     *
     * @param userId 포인트 지급할 유저 Id
     */
    void rewardPointForSignUp(Long userId);


    /**
     * 리워드 달성 시 포인트 적립 메서드
     *
     * @param userId 포인트 적립할 유저 Id
     * @param point  적립 될 포인트
     */
    void earnPoint(Long userId, int point);

    /**
     * 포인트 조회 메서드
     *
     * @param userId 포인트를 조회할 유저 Id
     */
    Point getPoint(Long userId);
}
