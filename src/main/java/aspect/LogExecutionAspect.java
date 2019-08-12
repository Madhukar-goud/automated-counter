package aspect;

import config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by madhukar on 17/05/19.
 */
@Aspect
@Component
@Slf4j
public class LogExecutionAspect {

    @Autowired
    AppConfig appConfig;

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        if (appConfig.isLogMethodExecutionTime()) {
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {} executed in {} ms ", joinPoint.getSignature(), executionTime);
        }
        return proceed;
    }

}
