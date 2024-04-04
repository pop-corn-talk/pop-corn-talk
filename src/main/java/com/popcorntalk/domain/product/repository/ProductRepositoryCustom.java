package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductGetResponseDto> findProducts(Pageable pageable);
}
