package com.popcorntalk.domain.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequestDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String image;
    @NotEmpty
    private String description;
    @NotNull
    private Long price;
}
