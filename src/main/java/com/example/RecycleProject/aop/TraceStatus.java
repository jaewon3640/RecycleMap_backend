package com.example.RecycleProject.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 시작 시간과 신분증을 묶은 것!
@Getter
@AllArgsConstructor
public class TraceStatus {
    private TraceId traceId;
    private Long startTimeMs;
    private String message;
}
