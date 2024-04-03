package com.popcorntalk.domain.exchange.service;

public interface ExchangeService {

    /**
     * 상품구매
     *
     * @param userId    구매하는 유저 id
     * @param productId 구매하려는 상품 id
     */
    void createExchange(Long userId, Long productId);
}
