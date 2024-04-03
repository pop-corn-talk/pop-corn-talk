package com.popcorntalk.domain.exchange.repository;

import com.popcorntalk.domain.exchange.dto.ExchangeGetResponseDto;
import com.popcorntalk.domain.exchange.entity.QExchange;
import com.popcorntalk.domain.product.entity.QProduct;
import com.popcorntalk.global.config.QuerydslConfig;
import com.querydsl.core.types.Projections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExchangeRepositoryImpl implements ExchangeRepositoryCustom {

    private final QuerydslConfig querydslConfig;
    QExchange qExchange = QExchange.exchange;
    QProduct qProduct = QProduct.product;

    @Override
    public List<ExchangeGetResponseDto> getExchanges(Long userId) {
        return querydslConfig.jpaQueryFactory()
            .select(Projections.fields(ExchangeGetResponseDto.class,
                qProduct.name.as("productName"),
                qExchange.productVoucherImage,
                qExchange.createdAt.as("exchangeDate")))
            .from(qExchange)
            .leftJoin(qProduct).on(qExchange.productId.eq(qProduct.id))
            .where(qExchange.userId.eq(userId))
            .orderBy(qExchange.createdAt.desc())
            .fetch();
    }
}
