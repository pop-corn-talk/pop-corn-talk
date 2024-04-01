package com.popcorntalk.domain.product.dto;

import com.popcorntalk.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReadResponseDto {

    private Long id;
    private String name;
    private String image;
    private String description;
    private Long price;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public ProductReadResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.createAt = product.getCreatedAt();
        this.modifiedAt = product.getModifiedAt();
    }
//
//    public static ProductReadResponseDto createOf(Product product){
//        return new ProductReadResponseDto(product);
//    }
}
