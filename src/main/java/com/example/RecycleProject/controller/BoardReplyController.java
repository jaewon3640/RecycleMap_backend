package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.BoardReplyDTO;
import com.example.RecycleProject.service.BoardReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/boardReply")

/*
    1. 답변 등록 2.답변 수정 3. 답변 삭제 4. 답변 조회(최신순으로)
 */
public class BoardReplyController {

    private final BoardReplyService boardReplyService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Long> registerBoardReply(
            @PathVariable Long boardId, @RequestBody @Valid BoardReplyDTO.Request request) {
        Long replyId = boardReplyService.registerBoardReply(boardId, request);
        return ResponseEntity.ok(replyId);
    }

    @DeleteMapping("/{boardReplyId}")
    public ResponseEntity<Void> deleteBoardReply(@PathVariable Long boardReplyId){

        boardReplyService.deleteBoardReply(boardReplyId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{boardReplyId}")
    public ResponseEntity<Void> updateBoardReply(
            @PathVariable Long boardReplyId, @RequestBody @Valid BoardReplyDTO.Request request
    ){
        boardReplyService.updateBoardReply(boardReplyId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<BoardReplyDTO.Response>> getAllBoardReply(
            @RequestParam Long boardId){
        List<BoardReplyDTO.Response> boardReplyList = boardReplyService.getBoardReplyList(boardId);
        return ResponseEntity.ok(boardReplyList);
    }

}
