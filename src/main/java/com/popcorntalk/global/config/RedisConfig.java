package com.popcorntalk.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableRedisRepositories
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Map<String, Boolean>> redisTemplate() {
        RedisTemplate<String, Map<String, Boolean>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Boolean.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Boolean.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name = "redisBlackListTemplate")
    public RedisTemplate<String, Object> redisBlackListTemplate(
        RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder =
            RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
                redisConnectionFactory());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(10L));

        builder.cacheDefaults(configuration);

        return builder.build();
    }

    @Bean(name = "postCacheManager")
    public CacheManager postCacheManager() {
        //JavaTimeModule 도 GenericJackson2JsonRedisSerializer에 등록해서 역직렬화를 하도록 설정
        //jackson의 다형성처리? NON_FINAL - 최상위수준의 추상클래스 및 인터페이스를 제외한 클래스의 다형성지원
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer =
            new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheManager.RedisCacheManagerBuilder builder =
            RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
                redisConnectionFactory());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    genericJackson2JsonRedisSerializer)) // Value Serializer 변경
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(10L));

        builder.cacheDefaults(configuration);

        return builder.build();
    }

    @Bean(name = "best3CacheManager")
    public CacheManager best3Cachemanager() {
        RedisCacheManager.RedisCacheManagerBuilder builder =
            RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
                redisConnectionFactory());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(10L));

        builder.cacheDefaults(configuration);

        return builder.build();
    }
}
