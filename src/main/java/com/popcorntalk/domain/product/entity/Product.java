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
    private String Name;

    @Column(nullable = false)
    private String Image;

    @Column(nullable = false, length = 50)
    private String Description;

    @Column(nullable = false)
    private Long Price;

    @Column(nullable = false)
    private DeletionStatus deletionStatus;

    private Product(String productName, String productImage, String productDescription,
        Long productPrice) {
        this.Name = productName;
        this.Image = productImage;
        this.Description = productDescription;
        this.Price = productPrice;
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

    public void update(ProductUpdateRequestDto productUpdateRequestDto) {
        this.Name = productUpdateRequestDto.getProductName();
        this.Image = productUpdateRequestDto.getProductImage();
        this.Description = productUpdateRequestDto.getProductDescription();
        this.Price = productUpdateRequestDto.getProductPrice();
    }
}
