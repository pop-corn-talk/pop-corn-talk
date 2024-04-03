package com.popcorntalk.domain.exchange.service;

import com.popcorntalk.domain.exchange.controller.ExchangeGetResponseDto;
import com.popcorntalk.domain.exchange.entity.Exchange;
import com.popcorntalk.domain.exchange.repository.ExchangeRepository;
import com.popcorntalk.domain.notification.service.NotificationService;
import com.popcorntalk.domain.point.service.PointService;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
    public void createExchange(Long userId, Long productId) {
        Product exchangeProduct = productService.getProduct(productId);
        Exchange exchange = Exchange.createOf(userId, productId, exchangeProduct.getVoucherImage());

        pointService.deductPointForPurchase(userId, exchangeProduct.getPrice());
        exchangeRepository.save(exchange);

        notificationService.notifyPurchase(userId, ADMIN_EMAIL, exchangeProduct.getVoucherImage());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeGetResponseDto> getExchanges(Long userId) {
        return exchangeRepository.getExchanges(userId);
    }
}
