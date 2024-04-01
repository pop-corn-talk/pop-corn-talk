package com.popcorntalk.domain.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReadResponseDto {

    private Long id;
    private String name;
    private String image;
    private String description;
    private Long price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
