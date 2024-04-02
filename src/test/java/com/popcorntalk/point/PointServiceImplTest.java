package com.popcorntalk.point;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.point.repository.PointRepository;
import com.popcorntalk.domain.point.service.PointRecordService;
import com.popcorntalk.domain.point.service.PointServiceImpl;
import com.popcorntalk.global.exception.customException.InsufficientPointException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointServiceImplTest {

    @InjectMocks
    private PointServiceImpl pointService;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointRecordService pointRecordService;

    private Point TEST_POINT;
    private Long TEST_USER_ID;
    private Long TEST_POINT_ID;
    private int POINT;

    @BeforeEach
    void setUp() {

        TEST_POINT_ID = 1L;
        TEST_USER_ID = 1L;
        POINT = 5000;

        TEST_POINT = Point.builder()
            .id(TEST_POINT_ID)
            .userId(TEST_USER_ID)
            .point(POINT)
            .build();
    }

    @Nested
    @DisplayName("포인트 차감 테스트")
    class DeductPoint {

        @Test
        @DisplayName("유저가 보유한 포인트가 상품의 포인트보다 적은 경우 포인트 차감 실패 테스트")
        void insufficientPointsDeductionFailure() {

            int TEST_PRICE = 6000;

            given(pointRepository.findByUserId(anyLong())).willReturn(Optional.of(TEST_POINT));

            assertThrows(InsufficientPointException.class, () -> {
                pointService.deductPointForPurchase(TEST_USER_ID, TEST_PRICE);
            });
        }
    }
}
