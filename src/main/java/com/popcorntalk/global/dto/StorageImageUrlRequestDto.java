package com.popcorntalk.global.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorageImageUrlRequestDto {

    @NotEmpty
    private String imageUrl;
}
