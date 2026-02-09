package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.FeedbackDTO;
import com.example.RecycleProject.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 피드백 등록
    @PostMapping("/{userId}")
    public ResponseEntity<Long> write(@RequestBody @Valid FeedbackDTO.Request request,
                                      @AuthenticationPrincipal Long userId) {

        Long feedbackId = feedbackService.saveFeedback(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackId);
    }

    // 수정 메서드

    @PutMapping("/{feedbackId}")
    public ResponseEntity<Void> update(@RequestBody @Valid FeedbackDTO.Request request,
                                       @AuthenticationPrincipal Long userId,
                                       @PathVariable Long feedbackId) {

        feedbackService.updateFeedback(feedbackId, userId, request);

        return ResponseEntity.ok().build();
    }

    // 피드백 삭제
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId,
                                       @PathVariable Long feedbackId) {

        feedbackService.deleteFeedback(feedbackId, userId);

        return ResponseEntity.ok().build();
    }

    /*
        피드백 조회
        1.특정 유저가 남긴 피드백을 최신순으로 조회
        2. Detail에 대한 피드백 조회
        3. 관리자용 모든 피드백을 조회하는 기능
     */

    @GetMapping("/my")
    public ResponseEntity<List<FeedbackDTO.Response>> findUserFeedback
            (@AuthenticationPrincipal Long userId) {

        List<FeedbackDTO.Response> userFeedback =
                feedbackService.findUserFeedback(userId);
        return ResponseEntity.ok(userFeedback);
    }

    @GetMapping("/trash/{trashDetailId}")
    public ResponseEntity<List<FeedbackDTO.Response>> findDetailFeedback
            (@PathVariable Long trashDetailId) {
        List<FeedbackDTO.Response> categoryFeedback = feedbackService.findCategoryFeedback(trashDetailId);

        return ResponseEntity.ok(categoryFeedback);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<FeedbackDTO.Response>> findAdminFeedback(){

        List<FeedbackDTO.Response> allFeedback = feedbackService.findAllFeedback();
        return ResponseEntity.ok(allFeedback);
    }
}
