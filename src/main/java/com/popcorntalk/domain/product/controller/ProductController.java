package com.popcorntalk.domain.product.controller;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.service.ProductService;
import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PostMapping
    public ResponseEntity<Void> createProduct(
        @RequestBody ProductCreateRequestDto productCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.createProduct(
            productCreateRequestDto,
            userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.deleteProduct(
            productId,
            userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
        @PathVariable Long productId,
        @RequestBody ProductUpdateRequestDto productUpdateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.updateProduct(
            productId,
            productUpdateRequestDto,
            userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<Page<ProductGetResponseDto>>> getProducts(
        @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponseDto.success(productService.getProducts(pageable)));
    }
}
