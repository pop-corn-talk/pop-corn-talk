package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.entity.QProduct;
import com.popcorntalk.global.config.QuerydslConfig;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final QuerydslConfig querydslConfig;
    QProduct qProduct = QProduct.product;

    @Override
    public List<ProductReadResponseDto> findProduct(Pageable pageable) {
        List<ProductReadResponseDto> ProductResponseDtoList = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(ProductReadResponseDto.class,
                qProduct.id,
                qProduct.name,
                qProduct.image,
                qProduct.description,
                qProduct.price,
                qProduct.createdAt,
                qProduct.modifiedAt))
            .from(qProduct)
            .where(qProduct.deletionStatus.eq(DeletionStatus.valueOf("N")))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        if (Objects.isNull(ProductResponseDtoList)) {
            throw new IllegalArgumentException("삼품이 없습니다.");
        }
        return ProductResponseDtoList;
    }

}
