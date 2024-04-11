package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import com.popcorntalk.domain.product.entity.QProduct;
import com.popcorntalk.global.config.QuerydslConfig;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;


@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final String HASH_KEY = "product";
    private final RedisTemplate<String, String> redisTemplate;
    private final QuerydslConfig querydslConfig;
    QProduct qProduct = QProduct.product;

    @Override
    public Page<ProductGetResponseDto> findProducts(Pageable pageable) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        List<ProductGetResponseDto> productResponseDtoList = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(ProductGetResponseDto.class,
                qProduct.id,
                qProduct.name,
                qProduct.image,
                qProduct.description,
                qProduct.price,
                qProduct.amount,
                qProduct.voucherImage,
                qProduct.createdAt,
                qProduct.modifiedAt))
            .from(qProduct)
            .where(qProduct.deletionStatus.eq(DeletionStatus.valueOf("N"))
                .and(qProduct.amount.ne(0)))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        if (Objects.isNull(productResponseDtoList)) {
            throw new IllegalArgumentException("상품이 없습니다.");
        }

        long productCount = querydslConfig.jpaQueryFactory()
            .select(qProduct)
            .from(qProduct)
            .where(qProduct.deletionStatus.eq(DeletionStatus.N)
                .and(qProduct.amount.ne(0)))
            .fetch().size();

        for (ProductGetResponseDto productDto : productResponseDtoList) {
            String amount = hashOperations.get(HASH_KEY, String.valueOf(productDto.getId()));
            productDto.updateAmount(Integer.parseInt(amount));
        }

        return new PageImpl<>(productResponseDtoList, pageable, productCount);
    }

}
