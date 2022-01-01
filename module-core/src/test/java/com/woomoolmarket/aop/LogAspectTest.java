package com.woomoolmarket.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogAspectTest {

    private final LogAspect logAspect = new LogAspect();

    private ProceedingJoinPoint proceedingJoinPoint;

    @BeforeEach
    void setUp() {
        proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
    }

    @Test
    @DisplayName("log 테스트")
    void logForRequestTest() throws Throwable {
    }
}