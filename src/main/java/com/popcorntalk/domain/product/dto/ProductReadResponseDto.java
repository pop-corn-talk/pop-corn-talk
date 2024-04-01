package com.popcorntalk.domain.product.dto;

import com.popcorntalk.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductReadResponseDto {

    private Long id;
    private String productName;
    private String productImage;
    private String productDescription;
    private Long productPrice;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public ProductReadResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productImage = product.getProductImage();
        this.productDescription = product.getProductDescription();
        this.productPrice = product.getProductPrice();
        this.createAt = product.getCreatedAt();
        this.modifiedAt = product.getModifiedAt();
    }
}
