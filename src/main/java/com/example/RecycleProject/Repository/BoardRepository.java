package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Board> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);

    Page<Board> findByUserAndTitleContainingOrderByCreatedAtDesc(User user, String title, Pageable pageable);
}
