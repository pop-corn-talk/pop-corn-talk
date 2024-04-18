package com.popcorntalk.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.product.service.ProductServiceImpl;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.mockData.MockData;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest extends MockData {

    @Mock
    private UserService userService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private HashOperations<String, String, String> hashOperations;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("상품 등록")
    void createProduct() {
        //given
        ReflectionTestUtils.setField(productService, "hashOperations", hashOperations);
        BDDMockito.given(productRepository.save(any())).willReturn(TEST_PRODUCT);

        //when
        productService.createProduct(TEST_PRODUCT_CREATE_REQUEST_DTO, TEST_USER_ID);
        //then
        BDDMockito.then(hashOperations).should().put(any(), any(), any());
        BDDMockito.then(productRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct() {
        //given
        ReflectionTestUtils.setField(productService, "hashOperations", hashOperations);
        BDDMockito.given(productRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_PRODUCT));

        //when
        productService.updateProduct(TEST_PRODUCT_ID, TEST_PRODUCT_UPDATE_REQUEST_DTO, TEST_USER_ID);

        //then
        BDDMockito.then(hashOperations).should().delete(any(), any());
        BDDMockito.then(hashOperations).should().put(any(), any(), any());

        assertEquals(TEST_PRODUCT_UPDATE_REQUEST_DTO.getAmount(), TEST_PRODUCT.getAmount());
        assertEquals(TEST_PRODUCT_UPDATE_REQUEST_DTO.getImage(), TEST_PRODUCT.getImage());
        assertEquals(TEST_PRODUCT_UPDATE_REQUEST_DTO.getPrice(), TEST_PRODUCT.getPrice());
        assertEquals(TEST_PRODUCT_UPDATE_REQUEST_DTO.getVoucherImage(), TEST_PRODUCT.getVoucherImage());
        assertEquals(TEST_PRODUCT_UPDATE_REQUEST_DTO.getAmount(), TEST_PRODUCT.getAmount());
        assertEquals(TEST_PRODUCT_UPDATE_REQUEST_DTO.getDescription(), TEST_PRODUCT.getDescription());
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() {
        //given
        ReflectionTestUtils.setField(productService, "hashOperations", hashOperations);
        BDDMockito.given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.ofNullable(TEST_PRODUCT));

        //when
        productService.deleteProduct(TEST_PRODUCT_ID, TEST_USER_ID);

        //then
        BDDMockito.then(hashOperations).should().delete(any(), any());
        assertEquals(DeletionStatus.Y, TEST_PRODUCT.getDeletionStatus());
    }
}
