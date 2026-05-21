package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.FeedbackDTO;
import com.example.RecycleProject.Repository.FeedbackRepository;
import com.example.RecycleProject.Repository.TrashDetailRepository;
import com.example.RecycleProject.Repository.UserRepository;
import com.example.RecycleProject.domain.Feedback;
import com.example.RecycleProject.domain.TrashDetail;
import com.example.RecycleProject.domain.User;
import com.example.RecycleProject.exception.AccessDeniedException;
import com.example.RecycleProject.exception.FeedbackNotFoundException;
import com.example.RecycleProject.exception.TrashDetailNotFoundException;
import com.example.RecycleProject.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final TrashDetailRepository trashRepository;

    @Transactional
    public Long saveFeedback(FeedbackDTO.Request dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을수 없습니다."));

        TrashDetail trashDetail = trashRepository.findById(dto.getTrashDetailId())
                .orElseThrow(() -> new TrashDetailNotFoundException("배출정보를 찾을수 없습니다."));

        Feedback save = feedbackRepository.save(dto.toEntity(user, trashDetail));
        return save.getId();
    }

    public Page<FeedbackDTO.Response> findUserFeedback(Long userId, Pageable pageable) {
        return feedbackRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(FeedbackDTO.Response::new);
    }

    @Transactional
    public void deleteFeedback(Long feedbackId, Long userId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("삭제할 문의가 존재하지 않습니다."));

        if (!feedback.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        feedbackRepository.delete(feedback);
    }

    @Transactional
    public void updateFeedback(Long feedbackId, Long userId, FeedbackDTO.Request dto) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("해당하는 문의가 존재하지 않습니다."));

        if (!feedback.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        feedback.update(dto.getFeedBackType(), dto.getContent());
    }

    public Page<FeedbackDTO.Response> findCategoryFeedback(Long detailId, Pageable pageable) {
        return feedbackRepository.findAllByTrashDetailIdOrderByCreatedAtDesc(detailId, pageable)
                .map(FeedbackDTO.Response::new);
    }

    public Page<FeedbackDTO.Response> findAllFeedback(Pageable pageable) {
        return feedbackRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(FeedbackDTO.Response::new);
    }
}
