package com.popcorntalk.domain.product.controller;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.service.ProductService;
import com.popcorntalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<Void> createProduct(
        @RequestBody ProductCreateRequestDto productCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.createProduct(productCreateRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
