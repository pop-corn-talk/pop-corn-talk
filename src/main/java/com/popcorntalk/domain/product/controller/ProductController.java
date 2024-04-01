package com.popcorntalk.domain.product.controller;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.service.ProductService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //삼품 등록
    @PostMapping
    public ResponseEntity<Void> createProduct(
        @RequestBody ProductCreateRequestDto productCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.createProduct(productCreateRequestDto, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.deleteProduct(productId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
        @PathVariable Long productId,
        @RequestBody ProductUpdateRequestDto productUpdateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.updateProduct(productId, productUpdateRequestDto,
            userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //상품 전체조회
    @GetMapping
    public ResponseEntity<CommonResponseDto<List<ProductReadResponseDto>>> getProduct() {
        List<ProductReadResponseDto> productReadResponseDtoLists = productService.getProduct();

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CommonResponseDto.success(productReadResponseDtoLists));
    }
}
