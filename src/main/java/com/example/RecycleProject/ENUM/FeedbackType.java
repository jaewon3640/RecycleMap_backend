package com.example.RecycleProject.ENUM;

import lombok.Getter;

@Getter
public enum FeedbackType {
    CLASSIFICATION_ERROR("분류 오류"),
    SCHEDULE_ERROR("배출 일정 오류"),
    CONTENT_ERROR("내용 오류"),
    MISSING_INFO("정보 누락"),
    ETC("기타");

    private final String description;
    FeedbackType(String description) { this.description = description; }
}