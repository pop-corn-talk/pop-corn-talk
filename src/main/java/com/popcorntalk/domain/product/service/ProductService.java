package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import java.util.List;

public interface ProductService {

    void createProduct(ProductCreateRequestDto productCreateRequestDto,
        UserRoleEnum userRoleEnum);

    void deleteProduct(Long productId, UserRoleEnum userRoleEnum);

    void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        UserRoleEnum userRoleEnum);

    List<ProductReadResponseDto> getProduct();
}
