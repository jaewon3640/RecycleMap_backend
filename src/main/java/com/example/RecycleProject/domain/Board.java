package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.BoardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

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

    public void changeStatus(BoardStatus status) {
        this.status = status;
    }

    /*
        연관 관계 편의 메서드
        유저와 게시물은 1대 N인데 유저에서도 게시물을 봐야되고, 게시물에서도 유저를 봐야된다.
     */
    public void setUser(User user){
        if (this.user != null) {
            this.user.getBoards().remove(this);
        }
        this.user = user;

        if(user != null && !this.user.getBoards().contains(this)){
            user.getBoards().add(this);
        }
    }

    // 생성 메서드
    public static Board createBoard(User user, String title, String content){
        Board board = new Board();
        board.title = title;
        board.content = content;
        board.status = BoardStatus.WAITING;

        board.setUser(user);

        return board;
    }


    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //***********Reply 관련 메서드 + 필드 ***********/

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardReply> boardReplyList = new ArrayList<>();

    public void updateStatus(BoardStatus status) {
        this.status = status;
    }

}
