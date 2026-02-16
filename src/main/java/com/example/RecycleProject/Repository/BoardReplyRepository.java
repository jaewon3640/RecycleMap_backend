package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    /*
    1. 답변 조회(최신순으로 볼수 있도록)
    2. 답변 작성(제공)
    3. 답변 수정
    4. 답변 삭제(알아서 제공)

     */

    List<BoardReply> findByBoardIdOrderByCreatedAtDesc(Long boardId);

}
