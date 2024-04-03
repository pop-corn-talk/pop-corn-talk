package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
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
    Page<ProductReadResponseDto> getProducts(Pageable pageable);
}
