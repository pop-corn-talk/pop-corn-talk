package com.popcorntalk.domain.point.service;

import com.popcorntalk.domain.point.entity.PointRecord;
import com.popcorntalk.domain.point.repository.PointRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointRecordService {

    private final PointRecordRepository pointRecordRepository;

    @Transactional
    public void createPointRecord(Long pointId, int previousPoint, int amount, int finalPoint) {

        PointRecord pointRecord = PointRecord.createOf(
            pointId, previousPoint, amount, finalPoint
        );
        pointRecordRepository.save(pointRecord);
    }
}
