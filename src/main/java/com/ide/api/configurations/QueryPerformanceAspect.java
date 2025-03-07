package com.ide.api.configurations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QueryPerformanceAspect {

    private static final Logger logger = LoggerFactory.getLogger(QueryPerformanceAspect.class);
    private static final long SLOW_QUERY_THRESHOLD_MS = 1000;

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public Object measureQueryPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if (executionTime > SLOW_QUERY_THRESHOLD_MS) {
            logger.warn("Requête lente détectée - Méthode: {} - Temps d'exécution: {} ms",
                    joinPoint.getSignature().toShortString(),
                    executionTime);
        }

        return result;
    }
}
