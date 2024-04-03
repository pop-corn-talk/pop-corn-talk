package com.popcorntalk.post.point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.popcorntalk.domain.point.entity.PointRecord;
import com.popcorntalk.domain.point.repository.PointRecordRepository;
import com.popcorntalk.domain.point.service.PointRecordServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointRecordServiceImplTest {

    @InjectMocks
    private PointRecordServiceImpl pointRecordService;

    @Mock
    private PointRecordRepository pointRecordRepository;

    @Test
    @DisplayName("포인트 기록 성공 테스트")
    void testSavePointRecord() {
        Long pointId = 1L;
        int previousPoint = 1000;
        int amount = 500;
        int finalPoint = 1500;

        pointRecordService.createPointRecord(pointId, previousPoint, amount, finalPoint);

        then(pointRecordRepository).should(times(1)).save(any(PointRecord.class));
    }
}
