package com.webbora.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Aspect for logging user requests before controller methods are executed.
 */

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final HttpServletRequest request;

    public LoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Logs user requests before controller methods are executed.
     *
     * @param joinPoint the join point representing the intercepted method
     */

    @Before("execution(* com.webbora.controller..*(..))")
    public void logRequest(JoinPoint joinPoint) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = Arrays.toString(joinPoint.getArgs());

        log.info("User Request: Method={}, URI={}, Params={}", method, uri, params);
    }
}