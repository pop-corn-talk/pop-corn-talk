package com.popcorntalk.domain.exchange.service;

import static com.popcorntalk.domain.product.service.ProductServiceImpl.HASH_KEY;

import com.popcorntalk.domain.exchange.dto.ExchangeGetResponseDto;
import com.popcorntalk.domain.exchange.entity.Exchange;
import com.popcorntalk.domain.exchange.repository.ExchangeRepository;
import com.popcorntalk.domain.notification.service.NotificationService;
import com.popcorntalk.domain.point.service.PointService;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.service.ProductService;
import com.popcorntalk.global.annotation.DistributedLock;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    private final RedisTemplate<String, String> redisTemplate;
    HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void initialize() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    @DistributedLock(lockName = "product", identifier = "productId", waitTime = 60, leaseTime = 4)
    public void createExchange(Long userId, Long productId) {
        Product product = productService.getProduct(productId);
        pointService.checkUserPoint(userId, product.getPrice());

        productAmount(product);

        pointService.deductPointForPurchase(userId, product.getPrice());
        Exchange exchange = Exchange.createOf(userId, product.getId(), product.getVoucherImage());
        exchangeRepository.save(exchange);

        notificationService.notifyPurchase(userId, ADMIN_EMAIL, product.getVoucherImage());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeGetResponseDto> getExchanges(Long userId) {
        return exchangeRepository.getExchanges(userId);
    }

    private void productAmount(Product product) {

        if (!hashOperations.hasKey(HASH_KEY, String.valueOf(product.getId()))) {
            throw new IllegalArgumentException("재고가 소진되었습니다.");
        }

        long incrementCount = hashOperations.increment(HASH_KEY,
            String.valueOf(product.getId()), -1);

        if (incrementCount == 0) {
            hashOperations.delete(HASH_KEY, String.valueOf(product.getId()));
            product.updateAmount(0);
            product.softDelete();
        }
    }
}
