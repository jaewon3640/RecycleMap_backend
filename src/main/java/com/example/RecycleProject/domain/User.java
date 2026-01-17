package com.example.RecycleProject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
// 테이블 전략을 통해서 짜자 Colmn은 확장에 용이 하지 않음
@Getter
@Setter //대신에 초기화 메서드를 설정하는것이 유리할듯 하다.
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(name="password_hash", nullable=false, length=255)
    private String password;

    @Column(length = 50)
    private String name;
    // 10글자 제한, null이여도 된다.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;
    /*
    enum 타입을 지정, 반드시 문자열을 이용한 타입으로 지정요망
    role은 USER로 고정한다. DB에서 ADMIN 변경
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    // 저장 로직이 자동으로 호출되도록

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    protected User() {}
}
