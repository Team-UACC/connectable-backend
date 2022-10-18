package com.backend.connectable.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class TimeCheckAspect {

    @Around("@annotation(timeCheck)")
    public Object checkTime(ProceedingJoinPoint joinPoint, TimeCheck timeCheck) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        long timeTaken = endTime - startTime;
        String methodName = MethodNameExtractor.generate(joinPoint.getSignature());
        log.info("timeTaken : {} at method {}", timeTaken, methodName);

        if (timeTaken > timeCheck.warningThreshold()) {
            log.warn("$$ TimeCheck Expired at = {} $$", methodName);
        }
        return result;
    }
}
