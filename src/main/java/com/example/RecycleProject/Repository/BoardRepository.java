package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // [N+1 Before] JOIN FETCH 제거 — 게시글 N개 조회 시 User SELECT가 N번 추가 발생
    @Query(value = "SELECT b FROM Board b WHERE b.user = :user ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b WHERE b.user = :user")
    Page<Board> findByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);

    @Query(value = "SELECT b FROM Board b ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT b FROM Board b WHERE b.title LIKE %:title% ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b WHERE b.title LIKE %:title%")
    Page<Board> findByTitleContainingOrderByCreatedAtDesc(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT b FROM Board b WHERE b.user = :user AND b.title LIKE %:title% ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b WHERE b.user = :user AND b.title LIKE %:title%")
    Page<Board> findByUserAndTitleContainingOrderByCreatedAtDesc(@Param("user") User user, @Param("title") String title, Pageable pageable);
}
