package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // [N+1 After] JOIN FETCH b.user — User(ToOne)를 한 번에 조인 조회 → 단일 쿼리.
    //   countQuery 는 fetch 없이 분리 유지 → 페이징 정상 (ToOne이라 메모리 페이징 문제 없음)
    @Query(value = "SELECT b FROM Board b JOIN FETCH b.user WHERE b.user = :user ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b WHERE b.user = :user")
    Page<Board> findByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.user WHERE b.title LIKE %:title% ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b WHERE b.title LIKE %:title%")
    Page<Board> findByTitleContainingOrderByCreatedAtDesc(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.user WHERE b.user = :user AND b.title LIKE %:title% ORDER BY b.createdAt DESC",
           countQuery = "SELECT COUNT(b) FROM Board b WHERE b.user = :user AND b.title LIKE %:title%")
    Page<Board> findByUserAndTitleContainingOrderByCreatedAtDesc(@Param("user") User user, @Param("title") String title, Pageable pageable);

    // [커서 페이징] board_id < lastId 로 PK 인덱스를 직접 seek → 위치와 무관하게 LIMIT 행만 읽음
    //   (OFFSET 페이징의 deep paging 문제 해결: OFFSET은 건너뛴 행도 모두 읽지만 커서는 안 읽음)
    //   JOIN FETCH b.user 로 목록과 동일하게 N+1 방지. Pageable 은 size(LIMIT) 지정용.
    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id < :lastId ORDER BY b.id DESC")
    List<Board> findByIdLessThanOrderByIdDesc(@Param("lastId") Long lastId, Pageable pageable);
}
