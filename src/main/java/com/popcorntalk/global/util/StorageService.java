package com.popcorntalk.global.util;

import com.popcorntalk.global.dto.StorageGetImageResponseDto;
import com.popcorntalk.global.dto.StorageImageUrlRequestDto;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * S3에 이미지 업로드 로직
     *
     * @param file 업로드할 이미지 파일
     * @return StorageGetImageResponseDto
     * @throws IOException
     */
    StorageGetImageResponseDto uploadFile(MultipartFile file) throws IOException;

    /**
     * S3에 업로드 된 이미지 삭제
     *
     * @param request 삭제할 키(이미지명)
     */
    void deleteImage(StorageImageUrlRequestDto request);
}
