package com.popcorntalk.domain.point.service;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.point.entity.PointRecord;
import com.popcorntalk.domain.point.repository.PointRecordRepository;
import com.popcorntalk.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private PointRecordRepository pointRecordRepository;
    private final int SIGNUP_REWARD = 1000;
    private final int INITIAL_POINT = 0;

    @Transactional
    public void processPurchase(Long userId, int purchaseAmount) {

        Point userPoint = getPoint(userId);

        if (userPoint.getPoint() >= purchaseAmount) {
            int newPointBalance = userPoint.getPoint() - purchaseAmount;
            userPoint.updatePoint(newPointBalance);

            PointRecord pointRecord = PointRecord.createOf(
                userPoint.getId(), userPoint.getPoint(), -purchaseAmount, newPointBalance
            );
            pointRecordRepository.save(pointRecord);

        } else {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
    }

    @Transactional
    public void rewardPointForSignUp(Long userId) {

        Point signupPoints = Point.createOf(userId, SIGNUP_REWARD);
        pointRepository.save(signupPoints);

        PointRecord pointRecord = PointRecord.createOf(
            signupPoints.getId(), INITIAL_POINT, +SIGNUP_REWARD, SIGNUP_REWARD
        );
        pointRecordRepository.save(pointRecord);
    }

    @Transactional
    public void earnPoint(Long userId, int point) {

        Point userPoint = getPoint(userId);
        int newPointBalance = userPoint.getPoint() + point;
        userPoint.updatePoint(newPointBalance);

        PointRecord pointRecord = PointRecord.createOf(
            userPoint.getId(), userPoint.getPoint(), +point, newPointBalance
        );
        pointRecordRepository.save(pointRecord);
    }

    public Point getPoint(Long userId) {
        return pointRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Point not found"));
    }
}
