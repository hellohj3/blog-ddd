package com.portfolio.blog.common.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // Controller 의 모든 메소드
    @Around("execution(* com.portfolio.blog.*.ui.*Controller.*(..))")
    public Object logging(ProceedingJoinPoint jp) throws Throwable {
        logger.info("start - " + jp.getSignature().getDeclaringTypeName() + "/" + jp.getSignature().getName());
        Object proceed = jp.proceed();
        logger.info("finished - " + jp.getSignature().getDeclaringTypeName() + "/" + jp.getSignature().getName());
        return proceed;
    }

}
