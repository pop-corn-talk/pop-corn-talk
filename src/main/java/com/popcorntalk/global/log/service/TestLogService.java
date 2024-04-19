package com.popcorntalk.global.log.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
public class TestLogService {
    public void testLogger(){
        for (int i = 0; i < 2000; i++) {
            log.debug("디버그 로그 메시지");
            log.info("정보 로그 메시지");
            log.warn("경고 로그 메시지");
            log.error("에러 로그 메시지");
            log.error("치명적 에러 로그 메시지");
        }
    }

}
