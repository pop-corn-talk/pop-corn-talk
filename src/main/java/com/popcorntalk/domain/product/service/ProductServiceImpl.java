package com.popcorntalk.domain.product.service;

import static com.popcorntalk.global.exception.ErrorCode.NOT_FOUND;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto, Long userId) {
        userService.validateAdminUser(userId);
        Product product = Product.createOf(productCreateRequestDto);

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId, Long userId) {
        userService.validateAdminUser(userId);
        Product productDelete = getProduct(productId);
        validateDeleteProduct(productDelete);

        productDelete.softDelete();
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId) {
        userService.validateAdminUser(userId);
        Product productUpdate = getProduct(productId);
        validateDeleteProduct(productUpdate);

        productUpdate.update(productUpdateRequestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReadResponseDto> getProduct(Pageable pageable) {

        return productRepository.findProduct(pageable);
    }

    private void validateDeleteProduct(Product product) {
        if (product.getDeletionStatus() == DeletionStatus.Y) {
            throw new IllegalArgumentException("삭제된 상품입니다.");
        }
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }
}
