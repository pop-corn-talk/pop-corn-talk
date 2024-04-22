package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    /**
     * 상품 생성
     *
     * @param productCreateRequestDto 생성할 상품의 내용
     * @param userId                  현재유저의 권한 체크할 유저 번호
     */
    void createProduct(ProductCreateRequestDto productCreateRequestDto,
        Long userId);

    /**
     * 상품 삭제
     *
     * @param productId 삭제할 상품의 번호
     * @param userId    현재유저의 권한 체크할 유저 번호
     */
    void deleteProduct(Long productId, Long userId);

    /**
     * 상품 수정
     *
     * @param productId               수정할 상품의 번호
     * @param productUpdateRequestDto 수정할 상품의 내용
     * @param userId                  현재유저의 권한 체크할 유저 번호
     */
    void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId);

    /**
     * 상품 전체조회
     *
     * @param pageable 페이징처리(기본값: size 10, page 0)
     * @return Page<ProductReadResponseDto>
     */
    Page<ProductGetResponseDto> getProducts(Pageable pageable);

    /**
     * Product Entity 조회
     *
     * @param productId 상품의 번호
     * @return Product
     */
    Product getProduct(Long productId);

    /**
     * 캐시에 등록되어있는 상품 번호 조회
     *
     * @param value 메소드 실행 검증 키
     * @return List<Long>
     */
    List<Long> getProductIdsByCache(String value);

    /**
     * 캐시에 등록된 수량을 실제 DB에 업데이트
     *
     * @param productId 캐시에 등록되어있는 상품번호
     */
    void updateAmount(Long productId);
}
