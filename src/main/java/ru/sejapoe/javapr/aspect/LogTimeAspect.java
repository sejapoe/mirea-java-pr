package ru.sejapoe.javapr.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@Aspect
public class LogTimeAspect {
    @SneakyThrows
    @Around("allServiceMethods()")
    public Object foo(ProceedingJoinPoint pjp) {
        Instant start = Instant.now();
        try {
            return pjp.proceed();
        } finally {
            Instant end = Instant.now();
            log.info("Method [{}] took {}ms", pjp.getSignature(), Duration.between(start, end).toMillis());
        }
    }

    @Pointcut("within(ru.sejapoe.javapr.service.*)")
    public void allServiceMethods() {

    }
}
