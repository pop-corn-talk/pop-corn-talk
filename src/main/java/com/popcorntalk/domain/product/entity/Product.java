package com.popcorntalk.domain.product.entity;

import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
}
