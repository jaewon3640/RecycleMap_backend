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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
/*
    1. feedback 저장
    2. feedback 조회
    3. feedback 삭제
    4. feedback 수정
 */
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final TrashDetailRepository trashRepository;

    /*
        여기서 UserId는 이제 보안으로 인해서 Controller에서 전달을 받자!
     */
    @Transactional
    public Long saveFeedback(FeedbackDTO.Request dto, Long userId){
        System.out.println("DEBUG: userId = " + userId);
        System.out.println("DEBUG: trashDetailId = " + dto.getTrashDetailId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을수 없습니다."));

        TrashDetail trashDetail =
                trashRepository.findById(dto.getTrashDetailId())
                        .orElseThrow(() -> new TrashDetailNotFoundException("배출정보를 찾을수 없습니다."));

        Feedback feedback = dto.toEntity(user, trashDetail);

        Feedback save = feedbackRepository.save(feedback);

        return save.getId();
    }

    // 특정 유저가 남긴 피드백을 최신순으로 조회
    public List<FeedbackDTO.Response> findUserFeedback(Long userId){

        List<Feedback> findUserFeedback =
                feedbackRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return findUserFeedback
                .stream()
                .map(feedback -> new FeedbackDTO.Response(feedback))
                .toList();
    }

    // 피드백 삭제
    @Transactional
    public void deleteFeedback(Long feedbackId, Long userId){
        Feedback feedback = feedbackRepository.findById(feedbackId)
                        .orElseThrow(()-> new FeedbackNotFoundException("삭제할 문의가 존재하지 않습니다."));

        // 이 피드백이 지금 요청한 유저의 것이 맞는지 확인해야 된다!
        if(!feedback.getUser().getId().equals(userId)){
            throw new AccessDeniedException();
        }
        feedbackRepository.delete(feedback);
    }

    // 피드백 수정
    @Transactional
    public void updateFeedback(Long feedbackId, Long userId, FeedbackDTO.Request dto){
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("해당하는 문의가 존재하지 않습니다.") );

        // 이 피드백이 지금 요청한 유저의 것이 맞는지 확인해야 된다!
        if(!feedback.getUser().getId().equals(userId)){
            throw new AccessDeniedException();
        }

        feedback.update(dto.getFeedBackType(), dto.getContent());
    }

    // 품목에 대한 피드백 조회
    public List<FeedbackDTO.Response> findCategoryFeedback(Long detailId){

        List<Feedback> findDetailFeedback =
                feedbackRepository.findAllByTrashDetailIdOrderByCreatedAtDesc(detailId);

        return findDetailFeedback
                .stream()
                .map(feedback -> new FeedbackDTO.Response(feedback))
                .toList();
    }

    // 관리자용 모든 피드백을 조회하는 기능
    public List<FeedbackDTO.Response> findAllFeedback(){
        List<FeedbackDTO.Response> list = feedbackRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(feedback -> new FeedbackDTO.Response(feedback))
                .toList();

        return list;
    }


}
