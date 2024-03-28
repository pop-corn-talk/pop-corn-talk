package com.popcorntalk.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequestDto {

    String productName;
    String productImage;
    String productDescription;
    Long productPrice;
}
