package com.popcorntalk.log;

import com.popcorntalk.global.log.service.LogService;
import com.popcorntalk.global.log.service.TestLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTest extends TimeManager{

    @Autowired
    private LogService LogService;

    private final TestLogService testLogService = new TestLogService();

    @Test
    @DisplayName("로그테스트 테스트 번호 : 1")
    void defaultLogTest(){
        testLogService.testLogger();
    }
    @Test
    @DisplayName("로그 파일 가져오기/  로그 bucket 에 올리기")
    void getFiles(){
        LogService.uploadLogs();
    }
}
