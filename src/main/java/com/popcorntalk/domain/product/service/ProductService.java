package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    public void createProduct(ProductCreateRequestDto productCreateRequestDto, User user) {
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
        Product product = Product.createOf(productCreateRequestDto);
        productRepository.save(product);
    }
}
