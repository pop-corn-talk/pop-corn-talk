package com.popcorntalk.domain.product.entity;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false, length = 50)
    private String productDescription;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private DeletionStatus deletionStatus;

    private Product(String productName, String productImage, String productDescription,
        Long productPrice) {
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Product createOf(ProductCreateRequestDto productCreateRequestDto) {
        return new Product(
            productCreateRequestDto.getProductName(),
            productCreateRequestDto.getProductImage(),
            productCreateRequestDto.getProductDescription(),
            productCreateRequestDto.getProductPrice()
        );
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }

    public void softUpdate(ProductUpdateRequestDto productUpdateRequestDto) {
        this.productName = productUpdateRequestDto.getProductName();
        this.productImage = productUpdateRequestDto.getProductImage();
        this.productDescription = productUpdateRequestDto.getProductDescription();
        this.productPrice = productUpdateRequestDto.getProductPrice();
    }
}
