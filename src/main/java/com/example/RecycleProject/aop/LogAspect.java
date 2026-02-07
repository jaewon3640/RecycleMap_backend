package com.example.RecycleProject.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
/*
    @Aspect : 횡단관심사를 핵심 로직과 분리하여,
    한 곳에서 처리하기 위한 개념이 등장한다.

    부가 기능을 별도로 관리 -> Advice
    이 부가 기능을 어디에 적용할까? -> Pointcut
 */

public class LogAspect {

    // 범위를 표현하기 위한 바구니
    @Pointcut("execution(* com.example.RecycleProject.controller..*.*(..))")
    public void cut() {
    }

    /*
    // 메서드를 실행하기 전에 로그는?
    @Before("cut()")
    public void beforeLogController(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        log.info("[AOP] 실행 메서드 : {}", method.getName());

        Object[] args = joinPoint.getArgs();
        //메서드에 들어가는 매개변수 배열을 읽어옴


        //매개변수 배열의 종류와 값을 출력
        for (Object arg : args) { // 12
            if (arg != null) {
                log.info("[AOP] Parameter: ({}) {}", arg.getClass().getSimpleName(),
                        arg);
            }
        }
    }

    //cut() 메서드가 종료되는 시점에 afterReturn() 메서드 실행
    //@AfterReturning 어노테이션의 returning 값과 afterReturn 매개변수 obj의 이름이 같아야 함
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterLog(JoinPoint joinPoint, Object returnObj) {
        log.info("[AOP] 종료 메서드: {}", joinPoint.getSignature().getName()); // 15
        log.info("[AOP] Return Value: {}", returnObj); //
    }

     */

    @Around("cut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimeMs = System.currentTimeMillis();
        String traceId = UUID.randomUUID().toString().substring(0, 8); // 짧은 ID 생성
        String message = joinPoint.getSignature().toShortString(); // 메서드 정보

        try {
            // [시작 로그]
            log.info("[{}] START : {}", traceId, message);

            // 파라미터 로그 (선택 사항)
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                log.info("[{}] ARGS  : {}", traceId, args);
            }

            // [핵심 로직 실행]
            Object result = joinPoint.proceed();

            // [종료 로그] 실행 시간 계산
            long stopTimeMs = System.currentTimeMillis();
            long resultTimeMs = stopTimeMs - startTimeMs;
            log.info("[{}] END   : {} | TIME: {}ms", traceId, message, resultTimeMs);

            return result;

        } catch (Exception e) {
            // [예외 로그]
            long stopTimeMs = System.currentTimeMillis();
            long resultTimeMs = stopTimeMs - startTimeMs;
            log.error("[{}] EX    : {} | TIME: {}ms | MSG: {}",
                    traceId, message, resultTimeMs, e.getMessage());
            throw e; // 예외는 반드시 다시 던져야 합니다.
        }
    }
}


