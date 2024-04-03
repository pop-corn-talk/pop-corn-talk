package com.popcorntalk.domain.exchange.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exchanges")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productVoucherImage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Exchange(Long userId, Long productId, String productVoucherImage) {
        this.userId = userId;
        this.productId = productId;
        this.productVoucherImage = productVoucherImage;
        this.createdAt = LocalDateTime.now();
    }

    public static Exchange createOf(Long userId, Long productId, String productVoucherImage) {
        return new Exchange(userId, productId, productVoucherImage);
    }
}
