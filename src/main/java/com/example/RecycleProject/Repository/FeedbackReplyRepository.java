package com.example.RecycleProject.Repository;


import com.example.RecycleProject.domain.BoardReply;
import com.example.RecycleProject.domain.FeedbackReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackReplyRepository extends JpaRepository<FeedbackReply, Long> {

    /*
        1. Feedback 답변
        2. Feedback 삭제
        3. Feedback 수정
        4. Feedback 조회(최신순으로 조회)
     */

    List<FeedbackReply> findByFeedbackIdOrderByCreatedAtDesc(Long feedbackId);
}
