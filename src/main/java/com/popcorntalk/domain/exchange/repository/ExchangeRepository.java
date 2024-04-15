package com.popcorntalk.domain.exchange.repository;

import com.popcorntalk.domain.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long>,
    ExchangeRepositoryCustom {

}
