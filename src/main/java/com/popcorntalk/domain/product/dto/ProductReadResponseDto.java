package com.popcorntalk.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductReadResponseDto {

    Long id;
    String productName;
    String productImage;
    String productDescription;
    Long productPrice;
}
