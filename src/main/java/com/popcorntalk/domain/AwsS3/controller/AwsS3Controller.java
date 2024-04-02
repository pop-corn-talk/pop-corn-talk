package com.popcorntalk.domain.AwsS3.controller;

import com.popcorntalk.global.dto.CommonResponseDto;
import com.popcorntalk.global.dto.StorageGetImageResponseDto;
import com.popcorntalk.global.dto.StorageImageUrlRequestDto;
import com.popcorntalk.global.util.StorageService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class AwsS3Controller {

    private final StorageService storageService;

    //S3 이미지 업로드
    @PostMapping
    public ResponseEntity<CommonResponseDto<StorageGetImageResponseDto>> createImageUrl(
        @RequestPart(value = "Image") MultipartFile file
    ) throws IOException {
        StorageGetImageResponseDto imageUrl = storageService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.success(imageUrl));
    }

    //S3 업로드 이미지 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteImage(
        @Valid @RequestBody StorageImageUrlRequestDto requestDto
    ) {
        storageService.deleteImage(requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
