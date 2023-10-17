package com.metalancer.backend.common.aop;


import java.util.UUID;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.metalancer.backend.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String txId = TransactionContext.getTransactionId();

        if (txId == null) {
            txId = UUID.randomUUID().toString();
            TransactionContext.setTransactionId(txId);
        }
        log.info("Transaction Id: {} {}.{}", txId, className, methodName);
    }

    @AfterReturning("execution(* com.metalancer.backend.*.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        String createdTxId = TransactionContext.getTransactionId();
        log.info("Query End Tx-id: {}", createdTxId);
        TransactionContext.clear();
    }

    @AfterThrowing("execution(* com.metalancer.backend.*.*(..))")
    public void afterThrowing(JoinPoint joinPoint) {
        String createdTxId = TransactionContext.getTransactionId();
        log.info("Exception End Tx-id: {}", createdTxId);
        TransactionContext.clear();
    }

}
