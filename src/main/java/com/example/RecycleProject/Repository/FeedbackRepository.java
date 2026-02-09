package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /*
        1. feedBack을 저장
        2. feedBack을 업데이트
        3. feedBack 조회(최신순으로만 조회하면 될듯)
        4. feedBack 삭제
     */

    //이때 3번의 경우에는 정렬 조건을 통해야 되므로 이때 쿼리 메서드가 필요

    //전체 피드백 조회
    List<Feedback> findAllByOrderByCreatedAtDesc();

    // 특정 유저가 남긴 피드백을 최신순으로 조회
    List<Feedback> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    // 특정 품목의 대한 피드백만 최신순으로 조회
    List<Feedback> findAllByTrashDetailIdOrderByCreatedAtDesc(Long detailId);

}
