package com.popcorntalk.domain.product.service;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.global.entity.DeletionStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto,
        UserRoleEnum userRoleEnum) {
        validateAdmin(userRoleEnum);
        Product product = Product.createOf(productCreateRequestDto);

        productRepository.save(product);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(Long productId, UserRoleEnum userRoleEnum) {
        validateAdmin(userRoleEnum);
        Product productDelete = validateDelete(productId);

        productDelete.softDelete();
    }

    //상품 수정
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        UserRoleEnum userRoleEnum) {
        validateAdmin(userRoleEnum);
        Product productUpdate = validateUpdate(productId);

        productUpdate.softUpdate(productUpdateRequestDto);
    }

    //상품 전체조회
    @Transactional
    public List<ProductReadResponseDto> getProduct() {
        List<Product> allReadProduct = productRepository.findAllByDeletionStatus(DeletionStatus.N);

        return allReadProduct.stream()
            .map(ProductReadResponseDto::new)
            .collect(Collectors.toList());
    }

    private Product validateUpdate(Long productId){
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("수정할 삼품이 없습니다."));
    }

    private Product validateDelete(Long productId){
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 삼품이 없습니다."));
    }

    private void validateAdmin(UserRoleEnum userRoleEnum) {
        if (userRoleEnum != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 권한이 필요합니다.");
        }
    }
}
