package com.popcorntalk.domain.product.service;

import static com.popcorntalk.global.exception.ErrorCode.NOT_FOUND;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.global.annotation.DistributedLock;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final String HASH_KEY = "product";
    private final UserService userService;
    private final ProductRepository productRepository;

    private final RedisTemplate<String, String> redisTemplate;
    HashOperations<String, String, String> hashOperations;

    @Value("${lambda.key}")
    private String functionKey;

    @PostConstruct
    private void initialize() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto,
        Long userId) {
        userService.validateAdminUser(userId);
        Product product = Product.createOf(productCreateRequestDto);
        Product saveProduct = productRepository.save(product);

        hashOperations.put(HASH_KEY, String.valueOf(saveProduct.getId()),
            String.valueOf(saveProduct.getAmount()));
    }

    @Override
    @Transactional
    @DistributedLock(lockName = HASH_KEY, identifier = "productId", waitTime = 60, leaseTime = 4)
    public void deleteProduct(Long productId,
        Long userId) {
        userService.validateAdminUser(userId);
        Product productDelete = getProduct(productId);
        productDelete.softDelete();

        hashOperations.delete(HASH_KEY, String.valueOf(productDelete.getId()));
    }

    @Override
    @Transactional
    @DistributedLock(lockName = HASH_KEY, identifier = "productId", waitTime = 60, leaseTime = 4)
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId) {
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

    @Override
    @Transactional(readOnly = true)
    public List<Long> getProductIdsByCache(String value) {
        if (functionKey.equals(value)) {
            Map<String, String> map = hashOperations.entries(HASH_KEY);
            List<Long> productIds = new ArrayList<>();
            for (Entry<String, String> entry : map.entrySet()) {
                productIds.add(Long.valueOf(entry.getKey()));
            }
            return productIds;
        }
        return null;
    }

    @Override
    @DistributedLock(lockName = HASH_KEY, identifier = "productId", waitTime = 60, leaseTime = 4)
    public void updateAmount(Long productId) {
        int amount = Integer.parseInt(
            Objects.requireNonNull(hashOperations.get(HASH_KEY, String.valueOf(productId))));
        Product product = getProduct(productId);
        product.updateAmount(amount);
        productRepository.save(product);
    }

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        if (product.getAmount() == 0) {
            throw new IllegalArgumentException("상품의 수량이 없습니다.");
        }
        if (product.getDeletionStatus() == DeletionStatus.Y) {
            throw new IllegalArgumentException("삭제된 상품입니다.");
        }
        return product;
    }
}
