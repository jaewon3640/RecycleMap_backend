package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.BoardStatus;
import com.example.RecycleProject.ENUM.FeedbackStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class FeedbackReply {

    @Id
    @GeneratedValue
    @Column(name ="feedbackreply_id")
    private Long id;

    private String replyContent;

    private String authorName;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updateAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
        feedback.getFeedbackReplies().add(this);
        feedback.updateStatus(FeedbackStatus.ANSWERED);
    }



    public static FeedbackReply createFeedbackReply(Feedback feedback, String replyContent, String authorName) {
        FeedbackReply feedbackReply = new FeedbackReply();
        feedbackReply.replyContent = replyContent;
        feedbackReply.authorName = authorName;

        feedbackReply.setFeedback(feedback);

        return feedbackReply;
    }

    public void update(String replyContent, String authorName) {
        this.replyContent = replyContent;
        this.authorName = authorName;
        this.updateAt = LocalDateTime.now();
    }
}
