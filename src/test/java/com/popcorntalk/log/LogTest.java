package com.popcorntalk.log;

import com.popcorntalk.global.log.service.TestLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTest extends TimeManager{


    // log4j 로거
    @Autowired
    private TestLogService testLogService;
    @Test
    @DisplayName("로그테스트 테스트 번호 : 1")
    void defaultLogTest(){
            testLogService.testNormalLogger();
    }
    @Test
    @DisplayName("로그 파일 가져오기")
    void getFiles(){
        testLogService.getLogs();
    }

}
