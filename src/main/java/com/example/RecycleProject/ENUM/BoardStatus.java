package com.example.RecycleProject.ENUM;

/*
    답변 대기중, 답변 완료 2가지 상태로
 */
public enum BoardStatus {
    WAITING("대기중"),
    ANSWERED("답변 완료");

    private final String description;

    BoardStatus(String description) {
        this.description = description;
    }
}
