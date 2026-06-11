package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT f FROM Feedback f JOIN FETCH f.user ORDER BY f.createdAt DESC",
           countQuery = "SELECT COUNT(f) FROM Feedback f")
    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT f FROM Feedback f JOIN FETCH f.user WHERE f.user.id = :userId ORDER BY f.createdAt DESC",
           countQuery = "SELECT COUNT(f) FROM Feedback f WHERE f.user.id = :userId")
    Page<Feedback> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT f FROM Feedback f JOIN FETCH f.user WHERE f.trashDetail.id = :detailId ORDER BY f.createdAt DESC",
           countQuery = "SELECT COUNT(f) FROM Feedback f WHERE f.trashDetail.id = :detailId")
    Page<Feedback> findAllByTrashDetailIdOrderByCreatedAtDesc(@Param("detailId") Long detailId, Pageable pageable);
}
