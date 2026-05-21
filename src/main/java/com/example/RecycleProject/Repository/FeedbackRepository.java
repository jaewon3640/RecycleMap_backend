package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Feedback> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Feedback> findAllByTrashDetailIdOrderByCreatedAtDesc(Long detailId, Pageable pageable);
}
