package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.global.entity.DeletionStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryRepository {

    List<Product> findAllByDeletionStatus(DeletionStatus deletionStatus);
}
