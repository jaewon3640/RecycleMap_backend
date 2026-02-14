package com.example.RecycleProject.DTO;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class BoardDTO {

    @Data
    public static class Request{

        @NotNull(message = "유저 Id는 필수 입니다.")
        private Long userId;
        
        @NotBlank(message = "제목은 필수 입니다.")
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하이여야 됩니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입니다.")
        @Size(min = 10, max = 1000, message = "내용은 10자 이상 1000자 이하이여야 됩니다.")
        private String content;


        //DTO를 엔티티로 변환시키어주는 메서드
        public Board toEntity(User user) {
            Board board = Board.createBoard(user, this.title, this.content);
            return board;
        }

    }

    @Data
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String title;
        private String content;
        private String authorEmail;
        private String authorName; // 작성자 이름
        private String status; // 게시글 상태
        private LocalDateTime createdAt; // 생성 일자

        // 엔티티를 DTO로 변환하는 생성자(이게 있어야 그냥 반환을 못하지)
        public Response(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.authorName = board.getUser().getName();
            this.status = board.getStatus().toString();
            this.createdAt = board.getCreatedAt();
            this.authorEmail = board.getUser().getEmail();
        }
    }

}
