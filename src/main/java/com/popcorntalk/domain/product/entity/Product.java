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

    @Column(nullable = true, length = 50)
    private String Description;

    @Column(nullable = false)
    private Long Price;

    @Column(nullable = false)
    private DeletionStatus deletionStatus;

    private Product(String Name, String Image, String Description,
        Long Price) {
        this.Name = Name;
        this.Image = Image;
        this.Description = Description;
        this.Price = Price;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Product createOf(ProductCreateRequestDto productCreateRequestDto) {
        return new Product(
            productCreateRequestDto.getName(),
            productCreateRequestDto.getImage(),
            productCreateRequestDto.getDescription(),
            productCreateRequestDto.getPrice()
        );
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }

    public void update(ProductUpdateRequestDto productUpdateRequestDto) {
        this.Name = productUpdateRequestDto.getName();
        this.Image = productUpdateRequestDto.getImage();
        this.Description = productUpdateRequestDto.getDescription();
        this.Price = productUpdateRequestDto.getPrice();
    }
}
