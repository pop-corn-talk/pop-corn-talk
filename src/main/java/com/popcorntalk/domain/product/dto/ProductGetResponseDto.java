package com.popcorntalk.domain.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetResponseDto {

    private Long id;
    private String name;
    private String image;
    private String description;
    private int price;
    private int amount;
    private String voucherImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
