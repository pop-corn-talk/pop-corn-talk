package com.popcorntalk.domain.point.service;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.point.entity.PointRecord;
import com.popcorntalk.domain.point.repository.PointRecordRepository;
import com.popcorntalk.domain.point.repository.PointRepository;
import com.popcorntalk.global.exception.ErrorCode;
import com.popcorntalk.global.exception.customException.InsufficientPointException;
import com.popcorntalk.global.exception.customException.PointNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private PointRecordRepository pointRecordRepository;
    private final int SIGNUP_REWARD = 1000;
    private final int INITIAL_POINT = 0;

    @Override
    @Transactional
    public void deductPointForPurchase(Long userId, int purchaseAmount) {

        Point userPoint = getPoint(userId);

        if (userPoint.getPoint() >= purchaseAmount) {
            int newPointBalance = userPoint.getPoint() - purchaseAmount;
            userPoint.updatePoint(newPointBalance);

            PointRecord pointRecord = PointRecord.createOf(
                userPoint.getId(), userPoint.getPoint(), -purchaseAmount, newPointBalance
            );
            pointRecordRepository.save(pointRecord);

        } else {
            throw new InsufficientPointException(ErrorCode.INSUFFICIENT_POINT);
        }
    }

    @Override
    @Transactional
    public void rewardPointForSignUp(Long userId) {

        Point signupPoints = Point.createOf(userId, SIGNUP_REWARD);
        pointRepository.save(signupPoints);

        PointRecord pointRecord = PointRecord.createOf(
            signupPoints.getId(), INITIAL_POINT, +SIGNUP_REWARD, SIGNUP_REWARD
        );
        pointRecordRepository.save(pointRecord);
    }

    @Override
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

    @Override
    public Point getPoint(Long userId) {
        return pointRepository.findByUserId(userId)
            .orElseThrow(() -> new PointNotFoundException(ErrorCode.POINT_NOT_FOUND));
    }
}
