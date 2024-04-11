package com.popcorntalk.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeTraceAspect {

    @Around("@annotation(com.popcorntalk.global.aop.TimeTrace)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            log.error("Exception occurred in method: {}", joinPoint.getSignature().toShortString(),
                ex);
            throw ex;
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("Method: {} Execution Time: {}ms", joinPoint.getSignature().toShortString(),
                timeMs);
        }
    }
}
