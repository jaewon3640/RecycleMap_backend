package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 1. 게시물 저장, 기본 제공

    // 2. 게시물 수정, Dirty Check

    // 3. 게시물 삭제, 기본 제공

    // 4. 게시물 단건 조회

    // 5. 게시물 전부 조회


    // 특정 유저의 모든 게시글을 최신순으로 조회
    List<Board> findByUserOrderByCreatedAtDesc(User user);

    // 전체 최신순 글로만 조회하는 기능
    List<Board> findAllByOrderByCreatedAtDesc();

    //검색어가 있다면? 검색어 제목 포함 조회
    List<Board> findByTitleContainingOrderByCreatedAtDesc(String title);

    // 5. (추가 팁) 특정 유저가 작성한 글 중에서 제목 검색이 필요한 경우
    List<Board> findByUserAndTitleContainingOrderByCreatedAtDesc(User user, String title);
}
