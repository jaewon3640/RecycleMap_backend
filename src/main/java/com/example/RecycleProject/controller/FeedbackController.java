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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    // 피드백 등록
    @PostMapping("/{userId}")
    public ResponseEntity<Long> write(@RequestBody @Valid FeedbackDTO.Request request,
                      @PathVariable Long userId){

        Long feedbackId = feedbackService.saveFeedback(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackId);
    }

    // 수정 메서드

    @PutMapping("/{feedbackId}")
    public void update(@RequestBody @Valid FeedbackDTO.Request request,
                       @AuthenticationPrincipal Long userId,
                       @PathVariable Long feedbackId){

        feedbackService.updateFeedback(feedbackId, userId, request);

    }
}
