package com.popcorntalk.log;

import com.popcorntalk.global.testlog.TestLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogSpeedTest extends TimeManager{


    // log4j 로거

    private TestLogService testLogService = new TestLogService();
    @Test
    @DisplayName("로그테스트 테스트 번호 : 1")
    void defaultLogTest(){
            testLogService.testNormalLogger();
    }
}
