package com.popcorntalk.domain.AwsS3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.popcorntalk.global.dto.StorageGetImageResponseDto;
import com.popcorntalk.global.dto.StorageImageUrlRequestDto;
import com.popcorntalk.global.util.StorageService;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AwsS3Service implements StorageService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.bucket.name}")
    private String bucket;

    @Override
    public StorageGetImageResponseDto uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        s3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        String imageUrl = s3Client.getUrl(bucket, fileName).toString();
        return new StorageGetImageResponseDto(imageUrl);
    }

    @Override
    public void deleteImage(StorageImageUrlRequestDto request) {
        s3Client.deleteObject(bucket, request.getImageUrl().split("/")[3]);
    }
}
