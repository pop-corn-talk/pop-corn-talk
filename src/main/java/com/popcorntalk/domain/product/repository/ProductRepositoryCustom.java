package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    List<ProductReadResponseDto> findProduct(Pageable pageable);

}
