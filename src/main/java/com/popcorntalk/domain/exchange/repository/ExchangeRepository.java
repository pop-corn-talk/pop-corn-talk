package com.popcorntalk.domain.exchange.repository;

import com.popcorntalk.domain.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Long>,
    ExchangeRepositoryCustom {

}
