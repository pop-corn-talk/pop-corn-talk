package com.popcorntalk.global.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class DataSourceConfig {

    // Main 데이터 소스 빈 이름
    public static final String MAIN_DATASOURCE = "mainDataSource";
    // Replica 데이터 소스 빈 이름
    public static final String REPLICA_DATASOURCE = "replicaDataSource";

    // Main 데이터 소스 빈 설정
    @Bean(MAIN_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.main.hikari")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    // Replica 데이터 소스 빈 설정
    @Bean(REPLICA_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.replica.hikari")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    // 라우팅 데이터 소스 빈 설정
    @Bean
    @DependsOn({MAIN_DATASOURCE, REPLICA_DATASOURCE})
    public DataSource routingDataSource(
        @Qualifier(MAIN_DATASOURCE) DataSource mainDataSource,
        @Qualifier(REPLICA_DATASOURCE) DataSource replicaDataSource) {

        RoutingDataSource routingDatasource = new RoutingDataSource();

        // 데이터 소스 맵 구성
        Map<Object, Object> dataSourceMap = new HashMap<>() {
            {
                put("main", mainDataSource);
                put("replica", replicaDataSource);
            }
        };

        // 데이터 소스 맵 설정
        routingDatasource.setTargetDataSources(dataSourceMap);
        // 기본 데이터 소스는 Main으로 설정
        routingDatasource.setDefaultTargetDataSource(mainDataSource);

        return routingDatasource;
    }

    // 기본 데이터 소스 빈 설정
    @Primary
    @Bean
    @DependsOn("routingDataSource")
    public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
