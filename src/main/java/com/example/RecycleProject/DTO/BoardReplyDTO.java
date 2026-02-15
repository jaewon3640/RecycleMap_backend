package com.example.RecycleProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

public class BoardReplyDTO {

    @Data
    static class Request{

        @NotBlank(message = "최소 5자 최대 10자의 답변을 달아주세요")
        @Size(min = 5, max = 10)
        String content;

        String authorName;

    }

    static class Response{

        private Long id;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;


    }
}
