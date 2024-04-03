package com.popcorntalk.domain.exchange.service;

import com.popcorntalk.domain.exchange.controller.ExchangeGetResponseDto;
import java.util.List;

public interface ExchangeService {

    /**
     * 상품구매
     *
     * @param userId    구매하는 유저 id
     * @param productId 구매하려는 상품 id
     */
    void createExchange(Long userId, Long productId);

    /**
     * 로그인한 유저 상품구매이력 전체조회
     *
     * @param userId 로그인한 유저 id
     * @return List<ExchangeGetResponseDto>
     */
    List<ExchangeGetResponseDto> getExchanges(Long userId);
}
