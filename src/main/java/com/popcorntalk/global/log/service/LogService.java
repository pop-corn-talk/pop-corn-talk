package com.popcorntalk.global.log.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.popcorntalk.global.exception.ErrorCode;
import com.popcorntalk.global.exception.customException.NotFoundException;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LogService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.log-bucket.name}")
    private String bucket;

    // 하루에 한번 작동 합니다.
    @Scheduled(cron = "1 0 0 * * *")
    public void uploadLogs() {
        File folder = new File("logs");
        String date = String.valueOf(LocalDate.now().minusDays(1));

        // 폴더 존재 하는지 확인
        if (!(folder.exists() && folder.isDirectory())) {
            log.error("로그 폴더가 존재 하지 않습니다.");
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        // 파일 필터를 사용하여 조건에 맞는 파일을 필터링
        File[] fileList = folder.listFiles((dir, name) -> name.contains(date) && new File(dir, name).isFile());

        // 파일이 null 이 아니고 비어있지 않은 경우에만 처리
        if (fileList != null && fileList.length > 0) {
            // 파일을 리스트에 추가
            List<File> files = new ArrayList<>(Arrays.asList(fileList));

            // 파일을 병렬로 S3에 업로드
            files.parallelStream().forEach(file -> {
                String key = file.getName(); // 이름을 키로 해서 중복이 없게끔.
                s3Client.putObject(new PutObjectRequest(bucket, key, file));
                log.info(key + " : s3 에 올리는중");
            });
        }
    }
}
