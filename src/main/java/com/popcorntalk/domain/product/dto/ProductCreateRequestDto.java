package com.popcorntalk.domain.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequestDto {

    @NotEmpty
    private String productName;
    @NotEmpty
    private String productImage;
    @NotEmpty
    private String productDescription;
    @NotNull
    private Long productPrice;

}
