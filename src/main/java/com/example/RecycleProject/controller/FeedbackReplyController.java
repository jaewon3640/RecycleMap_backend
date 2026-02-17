package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.BoardReplyDTO;
import com.example.RecycleProject.DTO.FeedbackReplyDTO;
import com.example.RecycleProject.Repository.FeedbackReplyRepository;
import com.example.RecycleProject.domain.FeedbackReply;
import com.example.RecycleProject.service.FeedbackReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback-reply")
/*
    1. 피드백 답변 저장
    2. 피드백 답변 삭제
    3. 피드백 답변 수정
    4. 피드백 답변 조회(최신순으로)
 */
public class FeedbackReplyController {

    private final FeedbackReplyService feedbackReplyService;

    @PostMapping("{feedbackId}")
    public ResponseEntity<Long> save(
            @RequestBody @Valid FeedbackReplyDTO.Request dto,
            @PathVariable Long feedbackId) {

        Long register = feedbackReplyService.register(feedbackId, dto);

        return ResponseEntity.ok(register);
    }

    @DeleteMapping("{feedbackReplyId}")
    public ResponseEntity<Void> delete(@PathVariable Long feedbackReplyId) {

        feedbackReplyService.delete(feedbackReplyId);

        return ResponseEntity.ok().build();
    }

    @PutMapping(("{feedbackReplyId}"))
    public ResponseEntity<Void> update(@RequestBody @Valid FeedbackReplyDTO.Request dto,
                                                @PathVariable Long feedbackReplyId) {

        feedbackReplyService.update(feedbackReplyId ,dto);
        return ResponseEntity.ok().build();
    }


    @GetMapping()
    public ResponseEntity<List<FeedbackReplyDTO.Response>> getAllFeedbackReply(
            @RequestParam Long feedbackId){
        List<FeedbackReplyDTO.Response> feedbackReplyList = feedbackReplyService.getFeedbackReplyList(feedbackId);
        return ResponseEntity.ok(feedbackReplyList);
    }
}

