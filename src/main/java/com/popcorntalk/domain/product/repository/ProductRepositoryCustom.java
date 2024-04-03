package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductReadResponseDto> findProducts(Pageable pageable);
}
