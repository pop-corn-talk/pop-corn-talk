package com.popcorntalk.log;

import java.sql.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.slf4j.Log4jLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogSpeedTest extends TimeManager {
    // log4j 로거
    private static final Logger log4jLogger = LogManager.getLogger(LogSpeedTest.class);

    @Test
    @DisplayName("log4jLogger 테스트 번호 : 1")
    void logback(){
        for (int i = 0; i < 2000; i++) {
            log4jLogger.debug("디버그 로그 메시지");
            log4jLogger.info("정보 로그 메시지");
            log4jLogger.warn("경고 로그 메시지");
            log4jLogger.error("에러 로그 메시지");
            log4jLogger.fatal("치명적 에러 로그 메시지");
        }
    }
    @Test
    @DisplayName("log4jLogger 테스트 번호 : 2")
    void log4jLogger(){
    }

}
