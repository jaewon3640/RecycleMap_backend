package com.example.RecycleProject.DTO;

import com.example.RecycleProject.ENUM.FeedbackType;
import com.example.RecycleProject.domain.Feedback;
import com.example.RecycleProject.domain.TrashDetail;
import com.example.RecycleProject.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FeedbackDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private FeedbackType feedBackType;

        @NotBlank(message = "내용을 입력해주세요")
        @Size(min = 10, max = 1000, message = "내용은 10자이상 1000자 이하로 작성해주세요")
        private String content;


        @NotNull(message = "배출정보를 입력해주세요.")
        private Long trashDetailId;

        public Feedback toEntity(User user, TrashDetail detail) {
            Feedback feedback = Feedback.createFeedback(user, detail, this.feedBackType, this.content);
            return feedback;
        }
    }


        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Response {
            private Long id;
            private String feedbackTypeDescription;
            private String content;
            private LocalDateTime createdAt;
            // 누가 이 피드백을 남겼는지 관리자가 파악해야 되기 때문에!
            private String userName;

            private Long trashDetailId;

            public Response(Feedback feedback) {
                this.id = feedback.getId();
                this.feedbackTypeDescription = feedback.getFeedbackType().getDescription(); // Enum의 한글명 등                this
                this.content = feedback.getContent();
                this.createdAt = feedback.getCreatedAt();
                this.userName = feedback.getUser().getName();
                this.trashDetailId = feedback.getTrashDetail().getId();
            }
        }
}
