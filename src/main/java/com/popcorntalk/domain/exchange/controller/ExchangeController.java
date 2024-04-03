package com.popcorntalk.domain.exchange.controller;


import com.popcorntalk.domain.exchange.dto.ExchangeGetResponseDto;
import com.popcorntalk.domain.exchange.service.ExchangeService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchanges")
public class ExchangeController {

    private final ExchangeService exchangeService;

    //상품 구매
    @PostMapping("/products/{productId}")
    public ResponseEntity<Void> createExchange(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId
    ) {
        exchangeService.createExchange(
            userDetails.getUser().getId(),
            productId
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //상품 구매 이력 전체 조회
    @GetMapping
    public ResponseEntity<CommonResponseDto<List<ExchangeGetResponseDto>>> getExchanges(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ExchangeGetResponseDto> responses = exchangeService.getExchanges(
            userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponseDto.success(responses));
    }
}
