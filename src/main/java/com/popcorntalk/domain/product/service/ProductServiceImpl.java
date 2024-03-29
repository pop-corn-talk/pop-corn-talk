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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    //상품 등록
    @Override
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto,
        UserRoleEnum userRoleEnum) {
        validateAdmin(userRoleEnum);
        Product product = Product.createOf(productCreateRequestDto);

        productRepository.save(product);
    }

    //상품 삭제
    @Override
    @Transactional
    public void deleteProduct(Long productId, UserRoleEnum userRoleEnum) {
        validateAdmin(userRoleEnum);
        Product productDelete = validateDelete(productId);
        validateDeleteProduct(productDelete);

        productDelete.softDelete();
    }

    //상품 수정
    @Override
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        UserRoleEnum userRoleEnum) {
        validateAdmin(userRoleEnum);
        Product productUpdate = validateUpdate(productId);
        validateDeleteProduct(productUpdate);

        productUpdate.softUpdate(productUpdateRequestDto);
    }

    //상품 전체조회
    @Override
    @Transactional(readOnly = true)
    public List<ProductReadResponseDto> getProduct() {
        List<Product> productLists = getProductDeleteStatus();

        return productLists.stream()
            .map(ProductReadResponseDto::new)
            .collect(Collectors.toList());
    }

    private void validateDeleteProduct(Product product) {
        if (product.getDeletionStatus() == DeletionStatus.Y) {
            throw new IllegalArgumentException("삭제된 상품입니다.");
        }
    }

    private List<Product> getProductDeleteStatus() {
        return productRepository.findAllByDeletionStatus(DeletionStatus.N);
    }

    private Product validateUpdate(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("수정할 상품이 없습니다."));
    }

    private Product validateDelete(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 상품이 없습니다."));
    }

    private void validateAdmin(UserRoleEnum userRoleEnum) {
        if (userRoleEnum != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 권한이 필요합니다.");
        }
    }
}
