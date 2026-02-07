package com.example.RecycleProject.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

// 로그의 신분증 (ID와 깊이 관리)
public class TraceId {
    private String id; // 예: "a1b2c3d4" (요청 고유 ID)
    private int level; // 예: 0(컨트롤러), 1(서비스), 2(레포지토리)

    public TraceId() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    public TraceId createNextId() { return new TraceId(id, level + 1); }
    public TraceId createPreviousId() { return new TraceId(id, level - 1); }
    public boolean isFirstLevel() { return level == 0; }
    public String getId() { return id; }
    public int getLevel() { return level; }
}

