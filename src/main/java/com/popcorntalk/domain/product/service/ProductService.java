package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto, User user) {
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
        Product product = Product.createOf(productCreateRequestDto);
        productRepository.save(product);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(Long productId, User user) {
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
        Product productDelete = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 삼품이 없습니다."));

        productDelete.softDelete();
    }

    //상품 수정
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        User user) {
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
        Product productUpdate = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("수정할 제품이 없습니다."));

        productUpdate.softUpdate(productUpdateRequestDto);
    }
}
