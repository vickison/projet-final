package com.ide.api.utilities;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeMonitorAspect {
    @Around("@annotation(TimeMonitor)")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.nanoTime();
        Object proceed = joinPoint.proceed();
        long exeTime = System.nanoTime() - start;
        System.out.println(joinPoint.getSignature().getName() + " took: " + exeTime + " ns");
        return proceed;
    }
}
