package com.example.RecycleProject.domain;

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

    //연관관계 편의 메서드(생성시 연관관계를 맺어야 된다!)


}
