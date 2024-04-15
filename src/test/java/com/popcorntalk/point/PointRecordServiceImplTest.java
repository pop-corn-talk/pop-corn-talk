package com.popcorntalk.point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.popcorntalk.domain.point.entity.PointRecord;
import com.popcorntalk.domain.point.repository.PointRecordRepository;
import com.popcorntalk.domain.point.service.PointRecordServiceImpl;
import com.popcorntalk.mockData.MockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointRecordServiceImplTest extends MockData {

    @InjectMocks
    private PointRecordServiceImpl pointRecordService;
    @Mock
    private PointRecordRepository pointRecordRepository;

    @Test
    @DisplayName("포인트 기록 성공 테스트")
    void testSavePointRecord() {

        int TEST_PREVIOUS_POINT = 1000;
        int TEST_AMOUNT = 500;
        int TEST_FINAL_POINT = 1500;

        pointRecordService.createPointRecord(
            TEST_POINT_ID,
            TEST_PREVIOUS_POINT,
            TEST_AMOUNT,
            TEST_FINAL_POINT
        );
        then(pointRecordRepository).should(times(1)).save(any(PointRecord.class));
    }
}
