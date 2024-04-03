package com.popcorntalk.post.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.point.repository.PointRepository;
import com.popcorntalk.domain.point.service.PointRecordService;
import com.popcorntalk.domain.point.service.PointServiceImpl;
import com.popcorntalk.global.exception.customException.InsufficientPointException;
import com.popcorntalk.global.exception.customException.NotFoundException;
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

        @Test
        @DisplayName("상품 구매시 유저 포인트 차감 성공 테스트")
        void insufficientPointsDeductionSuccess() {

            int TEST_PRICE = 4000;
            int expectedPoint = POINT - TEST_PRICE;

            given(pointRepository.findByUserId(anyLong())).willReturn(Optional.of(TEST_POINT));

            pointService.deductPointForPurchase(TEST_USER_ID, TEST_PRICE);
            int resultPoint = TEST_POINT.getPoint();

            assertEquals(expectedPoint, resultPoint);
        }
    }

    @Nested
    @DisplayName("포인트 적립 테스트")
    class EarnPoint {

        @Test
        @DisplayName("포인트 적립 성공 테스트")
        void earnPointSuccess() {

            int ADD_POINT = 1000;
            int expectedPoint = POINT + ADD_POINT;

            given(pointRepository.findByUserId(anyLong())).willReturn(Optional.of(TEST_POINT));
            pointService.earnPoint(TEST_USER_ID, ADD_POINT);

            int resultPoint = TEST_POINT.getPoint();
            assertEquals(expectedPoint, resultPoint);
        }
    }

    @Nested
    @DisplayName("회원가입 시 포인트 지급 테스트")
    class rewardPointForSignUp {

        @Test
        @DisplayName("회원가입 시 포인트 지급 성공 테스트")
        void rewardPointSuccess() {

            pointService.rewardPointForSignUp(TEST_USER_ID);

            then(pointRepository).should(times(1)).save(any(Point.class));
        }
    }

    @Test
    @DisplayName("해당 userId의 포인트가 존재하지 않을 때 포인트 반환 실패 테스트")
    void notFoundPoint() {

        Long NotExistUserId = 123L;

        assertThrows(NotFoundException.class, () -> {
            pointService.getPoint(NotExistUserId);
        });
    }
}
