package com.popcorntalk.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    // 현재 사용할 데이터 소스 키를 결정하는 메서드
    @Override
    protected Object determineCurrentLookupKey() {
        // 현재 트랜잭션이 읽기 전용인지 확인하여 데이터 소스를 결정
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            log.info("replicaDB");
            return "replica"; // 복제 데이터 소스 선택
        }
        log.info("mainDB");
        return "main"; // 메인 데이터 소스 선택
    }
}
