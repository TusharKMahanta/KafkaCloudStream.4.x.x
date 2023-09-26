package com.hh.kcs.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Aspect
@Component
public class CircuitBreakerAspect {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Around("@annotation(PauseOnFailure)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String binderName = joinPoint.getSignature().getName();
        Consumer<String> inner = (Consumer<String>) joinPoint.proceed();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(binderName);
        /*circuitBreaker.decorateConsumer(inner);
        Consumer<String> x = s -> {
            System.out.println("Hello Tushar");
            try {
                inner.accept(s);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };*/
        return circuitBreaker.decorateConsumer(inner);
    }
}
