package com.popcorntalk.domain.point.service;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.point.entity.PointRecord;
import com.popcorntalk.domain.point.repository.PointRecordRepository;
import com.popcorntalk.domain.point.repository.PointRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private PointRecordRepository pointRecordRepository;
    private final int SIGNUP_REWARD = 1000;

    public void processPurchase(Long userId, int purchaseAmount) {

        Point userPoint = getPoint(userId);

        if (userPoint.getPoint() >= purchaseAmount) {
            int newPointBalance = userPoint.getPoint() - purchaseAmount;
            userPoint.updatePoint(newPointBalance);

            PointRecord pointRecord = PointRecord.builder()
                .pointId(userPoint.getId())
                .amount(-purchaseAmount)
                .createdAt(LocalDateTime.now())
                .build();
            // 추후 상품 정보도 기록할 예정 -> point 필드에 상품 카테코리 enum 사용 예정
            pointRecordRepository.save(pointRecord);

        } else {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
    }

    public void rewardPointForSignUp(Long userId) {
        Point signupPoints = Point.createPoint(userId, SIGNUP_REWARD);
        pointRepository.save(signupPoints);
    }

    public void earnPoint(Long userId, int point) {
        Point userPoint = getPoint(userId);
        int newPointBalance = userPoint.getPoint() + point;
        userPoint.updatePoint(newPointBalance);
    }

    public Point getPoint(Long userId) {
        return pointRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Point not found"));
    }
}
