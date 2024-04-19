package com.popcorntalk.global.log.service;

import com.amazonaws.services.s3.AmazonS3;
import com.popcorntalk.global.exception.ErrorCode;
import com.popcorntalk.global.exception.customException.NotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TestLogService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.bucket.name}")
    private String bucket;

    public void getLogs() {
        List<File> files = new ArrayList<>();
        File folder = new File("logs");

        // 폴더 존재 하는지 확인
        if (folder.exists() && folder.isDirectory()) {
            File[] fileList = folder.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    // 파일 일때만 dir 토리가 아니라
                    if (file.isFile()) {
                        files.add(file);
                    }
                }
            }
        }
        else {
            log.fatal("로그 파일이 존재 하지 않습니다.!!!!");
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        for (File file : files) {
            String key = file.getName(); // You can customize the key as per your requirement
            System.out.println(key);
            //uploadFil(bucketName, key, file);
        }

    }

    public void testNormalLogger(){
        for (int i = 0; i < 2000; i++) {
            log.debug("디버그 로그 메시지");
            log.info("정보 로그 메시지");
            log.warn("경고 로그 메시지");
            log.error("에러 로그 메시지");
            log.error("치명적 에러 로그 메시지");
        }
    }
}
