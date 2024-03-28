package com.popcorntalk.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequestDto {

    String productName;
    String productImage;
    String productDescription;
    Long productPrice;

}
