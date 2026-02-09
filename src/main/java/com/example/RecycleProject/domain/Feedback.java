package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.FeedbackType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 연관관계 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_id")
    private TrashDetail trashDetail;

    public static Feedback createFeedback
            (User user, TrashDetail detail, FeedbackType feedBackType, String content){
        Feedback feedback = new Feedback();
        //연관관계 매핑(단방향이므로 필드만 설정)
        feedback.user = user;
        feedback.trashDetail = detail;

        //데이터 매핑
        feedback.feedbackType = feedBackType;
        feedback.content = content;

        return feedback;
    }

    public void update(FeedbackType feedbackType, String content) {
        if (feedbackType != null) {
            this.feedbackType = feedbackType;
        }
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }


}
