package com.popcorntalk.global.testlog;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Slf4j
public class TestLogService {
    private static final Logger log4jLogger = LogManager.getLogger(TestLogService.class);
    public void testNormalLogger(){
        for (int i = 0; i < 2000; i++) {
            log.debug("디버그 로그 메시지");
            log.info("정보 로그 메시지");
            log.warn("경고 로그 메시지");
            log.error("에러 로그 메시지");
            log.error("치명적 에러 로그 메시지");
        }
    }

    public void testlog4jLogger(){
        for (int i = 0; i < 2000; i++) {
            log4jLogger.debug("디버그 로그 메시지");
            log4jLogger.info("정보 로그 메시지");
            log4jLogger.warn("경고 로그 메시지");
            log4jLogger.error("에러 로그 메시지");
            log4jLogger.error("치명적 에러 로그 메시지");
        }
    }
}
