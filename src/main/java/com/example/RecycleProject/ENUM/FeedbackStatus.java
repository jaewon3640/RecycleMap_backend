package com.example.RecycleProject.ENUM;

public enum FeedbackStatus {
    WAITING("대기중"),
    ANSWERED("답변 완료");

    private final String description;

    FeedbackStatus(String description) {
        this.description = description;
    }
}
