package com.popcorntalk.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequestDto {

    private String productName;
    private String productImage;
    private String productDescription;
    private Long productPrice;
}
