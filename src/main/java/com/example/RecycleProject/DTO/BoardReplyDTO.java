package com.example.RecycleProject.DTO;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.BoardReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

public class BoardReplyDTO {

    @Data
    public static class Request{

        @NotBlank(message = "최소 5자 최대 10자의 답변을 달아주세요")
        @Size(min = 5, max = 10)
        String content;

        String authorName;

        public BoardReply toEntity(Board board){
            BoardReply boardReply = BoardReply.createBoardReply(board, this.content, this.authorName);
            return boardReply;
        }

    }


    @Data
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
