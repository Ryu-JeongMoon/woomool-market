package com.woomoolmarket.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log4j2
@Aspect
@Component
public class LogAspect {

    @Around("@within(com.woomoolmarket.aop.time.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(String.valueOf(joinPoint.getSignature()));
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        return proceed;
    }

    @AfterReturning(value = "@within(com.woomoolmarket.aop.exception.LogForException)", returning = "response")
    public void logForException(JoinPoint joinPoint, Object response) {
        log.error("method -> {}", joinPoint.getSignature().toShortString());
        log.error("target -> {}", joinPoint.getTarget());
        log.error("response -> {}", response);
    }
}
