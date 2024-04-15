package com.popcorntalk.domain.exchange.service;

import static com.popcorntalk.domain.product.service.ProductServiceImpl.HASH_KEY;

import com.popcorntalk.domain.exchange.dto.ExchangeGetResponseDto;
import com.popcorntalk.domain.exchange.entity.Exchange;
import com.popcorntalk.domain.exchange.repository.ExchangeRepository;
import com.popcorntalk.domain.notification.service.NotificationService;
import com.popcorntalk.domain.point.service.PointService;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private static final String ADMIN_EMAIL = "admin@admin.com";

    private final PointService pointService;
    private final ProductService productService;
    private final ExchangeRepository exchangeRepository;
    private final NotificationService notificationService;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, String> redisTemplate;
    HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void initialize() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    @Transactional
    public void createExchange(Long userId, Long productId) {
        Product product = productService.getProduct(productId);
        pointService.deductPointForPurchase(userId, product.getPrice());
        Exchange exchange = Exchange.createOf(userId, productId, product.getVoucherImage());
        exchangeRepository.save(exchange);
        lockProductAmount(product);

        notificationService.notifyPurchase(userId, ADMIN_EMAIL, product.getVoucherImage());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeGetResponseDto> getExchanges(Long userId) {
        return exchangeRepository.getExchanges(userId);
    }

    private void lockProductAmount(Product product) {
        String lockKey = "product_lock";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean lockAcquired = lock.tryLock(2, 2, TimeUnit.SECONDS);
            if (lockAcquired) {
                int count = product.getAmount();
                if (count > 0) {
                    Long incrementCount = hashOperations.increment(HASH_KEY,
                        String.valueOf(product.getId()), -1);
                    hashOperations.get(HASH_KEY, String.valueOf(product.getId()));

                    if (incrementCount == 0) {
                        hashOperations.delete(HASH_KEY, String.valueOf(product.getId()));
                        product.updateAmount(0);
                        product.softDelete();
                    }
                }
            } else {
                throw new RuntimeException("다시 시도해주세요.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}


