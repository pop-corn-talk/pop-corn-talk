package com.popcorntalk.domain.product.entity;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = true, length = 50)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String voucherImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeletionStatus deletionStatus;

    private Product(String name, String image, String description,
        int price, int amount, String voucherImage) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.voucherImage = voucherImage;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Product createOf(ProductCreateRequestDto productCreateRequestDto) {
        return new Product(
            productCreateRequestDto.getName(),
            productCreateRequestDto.getImage(),
            productCreateRequestDto.getDescription(),
            productCreateRequestDto.getPrice(),
            productCreateRequestDto.getAmount(),
            productCreateRequestDto.getVoucherImage()
        );
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }

    public void update(ProductUpdateRequestDto productUpdateRequestDto) {
        this.name = productUpdateRequestDto.getName();
        this.image = productUpdateRequestDto.getImage();
        this.description = productUpdateRequestDto.getDescription();
        this.price = productUpdateRequestDto.getPrice();
        this.amount = productUpdateRequestDto.getAmount();
        this.voucherImage = productUpdateRequestDto.getVoucherImage();
    }
}
