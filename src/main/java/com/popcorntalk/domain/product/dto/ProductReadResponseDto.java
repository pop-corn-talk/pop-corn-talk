package com.popcorntalk.domain.product.dto;

import com.popcorntalk.domain.product.entity.Product;
import lombok.AllArgsConstructor;
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

    public ProductReadResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productImage = product.getProductImage();
        this.productDescription = product.getProductDescription();
        this.productPrice = product.getProductPrice();
    }
}
