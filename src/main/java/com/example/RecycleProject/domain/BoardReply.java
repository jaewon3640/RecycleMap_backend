package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.BoardStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String replyContent;

    private String authorName;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    // Reply를 만들어서 반환해주는 메서드
    public static void toEntity() {

    }

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }


    @PreUpdate
    // 데이터가 수정될 때마다 자동으로 실행됨!
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }


    // 이떄 생성시에 연관관계를 매핑! 양방향이기 때문이지!

    public static BoardReply createBoardReply(Board board, String replyContent, String authorName) {
        BoardReply boardReply = new BoardReply();

        boardReply.replyContent = replyContent;
        boardReply.authorName = authorName;
        boardReply.setBoard(board);

        return boardReply;
    }

    //연관관계 편의 메서드(생성시 연관관계를 맺어야 된다!)
    public void setBoard(Board board) {
        this.board = board;
        board.getBoardReplyList().add(this);
        board.updateStatus(BoardStatus.ANSWERED);
    }

    public void update(String replyContent, String authorName) {
        this.replyContent = replyContent;
        this.authorName = authorName;
    }
}
