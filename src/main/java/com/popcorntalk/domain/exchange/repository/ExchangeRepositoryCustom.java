package com.popcorntalk.domain.exchange.repository;

import com.popcorntalk.domain.exchange.dto.ExchangeGetResponseDto;
import java.util.List;

public interface ExchangeRepositoryCustom {

    /**
     * 로그인한 유저 상품구매이력 전체조회
     *
     * @param userId 로그인한 유저 id
     * @return List<ExchangeGetResponseDto>
     */
    List<ExchangeGetResponseDto> getExchanges(Long userId);
}
