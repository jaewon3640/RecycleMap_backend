package com.example.RecycleProject.DTO;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.BoardReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardReplyDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        @NotBlank(message = "최소 5자 최대 1000자의 답변을 달아주세요")
        @Size(min = 5, max = 1000)
        private String replyContent;

        private String authorName;

        public BoardReply toEntity(Board board){
            BoardReply boardReply = BoardReply.createBoardReply(board, this.replyContent, this.authorName);
            return boardReply;
        }

    }


    @Data
    @NoArgsConstructor
    public static class Response{

        private Long id;
        private String content;
        private String authorName;
        private LocalDateTime createdAt;

        public Response(BoardReply boardReply) {
            this.id = boardReply.getId()   ;
            this.content = boardReply.getReplyContent();
            this.authorName = boardReply.getAuthorName();
            this.createdAt = boardReply.getCreatedAt();
        }
    }


}
