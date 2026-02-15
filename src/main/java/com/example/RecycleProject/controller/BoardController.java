package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.BoardDTO;
import com.example.RecycleProject.Repository.BoardRepository;
import com.example.RecycleProject.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j

public class BoardController {
    private final BoardService boardService;

    //1. 게시판 작성
    @PostMapping("/write")
    public ResponseEntity<Long> write(
            @RequestBody @Valid BoardDTO.Request request
    ){
        Long write = boardService.write(request);
        return ResponseEntity.ok(write);
    }


    //2. 게시글 상세 조회, 아이디에 따라서 각각에 다른 게시글을 조회해야 되므로 경로로 받자
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO.Response> getBoard(
            @PathVariable Long id){
        log.info("게시글 상세 조회 요청 - ID: {}", id);
        BoardDTO.Response oneBoard = boardService.findOneBoard(id);

        log.info("조회된 게시글 데이터: Title={}, Content={}, Author={}",
                oneBoard.getTitle(), oneBoard.getContent(), oneBoard.getAuthorName());

        return ResponseEntity.ok(oneBoard);
    }

    // 3. 제목으로 검색
    @GetMapping("/search-name")
    public ResponseEntity<List<BoardDTO.Response>> search(
            @RequestParam(required = false) String title
    ){
        System.out.println("검색 키워드: " + (title == null ? "null" : "'" + title + "'"));

        List<BoardDTO.Response> responses =
                boardService.findAllorSearch(title);

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteBoard(
            @PathVariable Long id
    ){
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Long> updateBoard(
            @PathVariable Long boardId, String email,
            @RequestBody  BoardDTO.Request request){
        boardService.updateBoard(boardId, email, request);
        return ResponseEntity.ok().build();
    }

    // ********************관리자  기능********************//
    /*
    1.) 게시판의 대한 답변 기능
    2. ) 답변 수정
    3. ) 답변 삭제
    4. ) 이때 사용자의 게시글의 상태를 WAIting에서 답변 완료로 변환할것

     */


}
