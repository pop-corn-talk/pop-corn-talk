package com.popcorntalk.domain.exchange.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeGetResponseDto {

    private String productName;
    private String productVoucherImage;
    private LocalDateTime exchangeDate;
}
