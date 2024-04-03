package com.popcorntalk.domain.exchange.service;

import com.popcorntalk.domain.exchange.dto.ExchangeGetResponseDto;
import com.popcorntalk.domain.exchange.entity.Exchange;
import com.popcorntalk.domain.exchange.repository.ExchangeRepository;
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

    private final PointService pointService;
    private final ProductService productService;
    private final ExchangeRepository exchangeRepository;

//    추후 알림 전송 로직에서 사용
//    private static final String ADMIN_EMAIL = "admin@admin.com";

    @Override
    @Transactional
    public void createExchange(Long userId, Long productId) {
        Product exchangeProduct = productService.getProduct(productId);
        Exchange exchange = Exchange.createOf(userId, productId, exchangeProduct.getVoucherImage());

        pointService.deductPointForPurchase(userId, exchangeProduct.getPrice());
        exchangeRepository.save(exchange);

//    추후 알림 전송 로직에서 사용
//    받는사람: userid
//    보낸사람: ADMIN_EMAIL
//    알림내용: exchangeProduct.getProoductVoucherImage() + "교환하신 교환권입니다."
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeGetResponseDto> getExchanges(Long userId) {
        return exchangeRepository.getExchanges(userId);
    }
}
