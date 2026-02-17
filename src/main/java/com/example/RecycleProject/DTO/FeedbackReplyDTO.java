package com.example.RecycleProject.DTO;

import com.example.RecycleProject.domain.BoardReply;
import com.example.RecycleProject.domain.Feedback;
import com.example.RecycleProject.domain.FeedbackReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FeedbackReplyDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{

        @NotBlank(message = "내용을 입력해주세요")
        @Size(min = 10, max = 1000, message = "내용은 10자이상 1000자 이하로 작성해주세요")
        private String content;

        private String authorName;

        public FeedbackReply toEntity(Feedback feedback){
            FeedbackReply feedbackReply = FeedbackReply.createFeedbackReply(feedback, this.content, this.authorName);
            return feedbackReply;
        }

    }

    @Data
    public static class Response{

        private Long id;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;

        public Response(FeedbackReply feedbackReply){
            this.id = feedbackReply.getId();
            this.content = feedbackReply.getReplyContent();
            this.authorName = feedbackReply.getAuthorName();
            this.createdAt = feedbackReply.getCreatedAt();
        }


    }
}
