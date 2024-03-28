package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
