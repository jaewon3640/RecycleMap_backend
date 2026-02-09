package com.example.RecycleProject.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogTrace {
    // 여러 쓰레드(사용자)가 섞이지 않게 '내 주머니'에만 신분증을 보관함
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    public TraceStatus begin(String message) {
        // 1. 신분증 확인 (없으면 새로 만들고, 있으면 level만 높임)
        TraceId traceId = (traceIdHolder.get() == null) ? new TraceId() : traceIdHolder.get().createNextId();
        traceIdHolder.set(traceId);

        long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace("-->", traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    public void end(TraceStatus status) {
        long resultTimeMs = System.currentTimeMillis() - status.getStartTimeMs();
        log.info("[{}] {}{} time={}ms", status.getTraceId().getId(), addSpace("<--", status.getTraceId().getLevel()), status.getMessage(), resultTimeMs);
        releaseTraceId();
    }

    private void releaseTraceId() {
        if (traceIdHolder.get().isFirstLevel())
            traceIdHolder.remove(); // 마지막 단계면 주머니 비우기
        else
            traceIdHolder.set(traceIdHolder.get().createPreviousId()); // 한 단계 밖으로 나감
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++)
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        return sb.toString();
    }

    // 예외 발생 시 호출되는 메서드
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    // 종료와 예외 처리를 공통으로 담당하는 로직
    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace("<--", traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace("<X-", traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
        releaseTraceId(); // 매우 중요: 레벨을 낮추거나 ThreadLocal을 비움
    }
}
