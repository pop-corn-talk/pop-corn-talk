package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import java.util.List;

public interface ProductService {

    void createProduct(ProductCreateRequestDto productCreateRequestDto,
        Long userId);

    void deleteProduct(Long productId, Long userId);

    void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId);

    List<ProductReadResponseDto> getProduct();
}
