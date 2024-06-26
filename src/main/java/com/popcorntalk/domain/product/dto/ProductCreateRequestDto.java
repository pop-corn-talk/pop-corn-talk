package com.popcorntalk.domain.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String image;
    @NotEmpty
    private String description;
    @NotNull
    private int price;
    @NotNull
    private int amount;
    @NotEmpty
    private String voucherImage;
}
