package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.FeedbackDTO;
import com.example.RecycleProject.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/save")
    public ResponseEntity<Long> write(@RequestBody @Valid FeedbackDTO.Request request,
                                      @AuthenticationPrincipal Long userId) {
        if (userId == null) {
            log.error("인증된 사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("컨트롤러에서 확인된 userId: {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.saveFeedback(request, userId));
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<Void> update(@RequestBody @Valid FeedbackDTO.Request request,
                                       @AuthenticationPrincipal Long userId,
                                       @PathVariable Long feedbackId) {
        feedbackService.updateFeedback(feedbackId, userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId,
                                       @PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<Page<FeedbackDTO.Response>> findUserFeedback(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(feedbackService.findUserFeedback(userId, PageRequest.of(page, size)));
    }

    @GetMapping("/trash/{trashDetailId}")
    public ResponseEntity<Page<FeedbackDTO.Response>> findDetailFeedback(
            @PathVariable Long trashDetailId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(feedbackService.findCategoryFeedback(trashDetailId, PageRequest.of(page, size)));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<FeedbackDTO.Response>> findAdminFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(feedbackService.findAllFeedback(PageRequest.of(page, size)));
    }
}
