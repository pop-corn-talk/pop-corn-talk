package com.popcorntalk.log;

import com.popcorntalk.domain.user.controller.UserController;
import com.popcorntalk.domain.user.service.UserService;
import com.popcorntalk.domain.user.service.UserServiceImpl;
import com.popcorntalk.global.testlog.TestLogService;
import com.popcorntalk.mockData.MockData;
import java.sql.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.slf4j.Log4jLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogSpeedTest extends TimeManager{


    // log4j 로거
    private static final Logger log4jLogger = LogManager.getLogger(LogSpeedTest.class);

    private TestLogService testLogService = new TestLogService();
    @Test
    @DisplayName("defaultLogTest 테스트 번호 : 1")
    void defaultLogTest(){
        testLogService.testNormalLogger();
    }

    @Test
    @DisplayName("defaultLogTest 테스트 번호 : 1")
    void log4jLogTest(){
        testLogService.testlog4jLogger();
    }

    @Test
    @DisplayName("defaultLogTest 테스트 번호 : 2")
    void defaultLogTest2(){
        testLogService.testNormalLogger();
    }
    @Test
    @DisplayName("defaultLogTest 테스트 번호 : 2")
    void log4jLogTest2(){
        testLogService.testlog4jLogger();
    }
}
