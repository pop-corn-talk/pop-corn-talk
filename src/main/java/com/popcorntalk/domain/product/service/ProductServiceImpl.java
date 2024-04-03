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

        productDelete.softDelete();
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId) {
        userService.validateAdminUser(userId);
        Product productUpdate = getProduct(productId);

        productUpdate.update(productUpdateRequestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReadResponseDto> getProducts(Pageable pageable) {

        return productRepository.findProducts(pageable);
    }

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        if(product.getDeletionStatus() == DeletionStatus.Y){
             throw new IllegalArgumentException("삭제된 상품입니다.");
         }
        return product;
    }
}
