package com.popcorntalk.global.aop;

import com.popcorntalk.global.annotation.DistributedLock;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "DistributedLock 설정")
@AllArgsConstructor
public class DistributedLockAop {

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.popcorntalk.global.annotation.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String baseKey = distributedLock.lockName();
        String dynamicKey = generateDynamicKey(signature.getParameterNames(), joinPoint.getArgs(),
            distributedLock.identifier());
        String key = baseKey + " : " + dynamicKey;
        RLock lock = redissonClient.getFairLock(key);

        log.info("{} - 락 획득 시도", key);
        try {
            boolean lockAcquired = lock.tryLock(distributedLock.waitTime(),
                distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!lockAcquired) {
                log.info("{} - 락 획득 실패", key);
                throw new IllegalArgumentException(key + " - RLock 획득 실패");
            }

            log.info("{} - 락 획득 성공", key);
            return aopForTransaction.proceed(joinPoint);
        } finally {
            try {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
                log.info("{} - 락 반납", key);
            } catch (IllegalMonitorStateException e) {
                log.info(e + baseKey + dynamicKey);
            }
        }
    }

    private String generateDynamicKey(String[] parameterNames, Object[] args, String
        identifier) {
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(identifier)) {
                return String.valueOf(args[i]);
            }
        }
        throw new IllegalArgumentException("해당하는 identifier가 없습니다.");
    }
}
