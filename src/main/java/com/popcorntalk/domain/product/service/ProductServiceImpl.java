package com.popcorntalk.domain.product.service;

import static com.popcorntalk.global.exception.ErrorCode.NOT_FOUND;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private static final String HASH_KEY = "product";
    private final UserService userService;
    private final ProductRepository productRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto, Long userId) {
        userService.validateAdminUser(userId);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        Product product = Product.createOf(productCreateRequestDto);
        Product svaedProduct = productRepository.save(product);

        hashOperations.put(HASH_KEY, String.valueOf(svaedProduct.getId()),
            String.valueOf(svaedProduct.getAmount()));
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId, Long userId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        userService.validateAdminUser(userId);
        Product productDelete = getProduct(productId);
        productDelete.softDelete();

        hashOperations.delete(HASH_KEY, String.valueOf(productDelete.getId()));
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        userService.validateAdminUser(userId);
        Product productUpdate = getProduct(productId);
        productUpdate.update(productUpdateRequestDto);

        hashOperations.delete(HASH_KEY, String.valueOf(productUpdate.getId()));
        hashOperations.put(HASH_KEY, String.valueOf(productUpdate.getId()),
            String.valueOf(productUpdate.getAmount()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductGetResponseDto> getProducts(Pageable pageable) {

        return productRepository.findProducts(pageable);
    }

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        if (product.getDeletionStatus() == DeletionStatus.Y) {
            throw new IllegalArgumentException("삭제된 상품입니다.");
        }
        if (product.getAmount() == 0) {
            throw new IllegalArgumentException("상품의 수량이 없습니다.");
        }
        return product;
    }
}
