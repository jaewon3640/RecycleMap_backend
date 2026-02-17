package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.BoardReplyDTO;
import com.example.RecycleProject.DTO.FeedbackReplyDTO;
import com.example.RecycleProject.ENUM.BoardStatus;
import com.example.RecycleProject.Repository.BoardReplyRepository;
import com.example.RecycleProject.Repository.FeedbackReplyRepository;
import com.example.RecycleProject.Repository.FeedbackRepository;
import com.example.RecycleProject.domain.Feedback;
import com.example.RecycleProject.domain.FeedbackReply;
import com.example.RecycleProject.exception.FeedbackNotFoundException;
import com.example.RecycleProject.exception.ReplyNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j

/*
        1. Feedback 답변
        2. Feedback 삭제
        3. Feedback 수정
        4. Feedback 조회(최신순으로 조회)
     */

public class FeedbackReplyService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackReplyRepository feedbackReplyRepository;

    @Transactional
    public Long register(Long feedbackId, FeedbackReplyDTO.Request dto) {

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("해당하는 피드백을 찾을수 없습니다."));

        FeedbackReply entity = dto.toEntity(feedback);

        FeedbackReply save = feedbackReplyRepository.save(entity);

        return save.getId();
    }

    @Transactional
    public void delete(Long feedbackReplyId) {
        FeedbackReply feedbackReply = feedbackReplyRepository.findById(feedbackReplyId)
                .orElseThrow(() -> new ReplyNotFoundException("해당하는 답변을 찾을수 없습니다."));

        Feedback feedback = feedbackReply.getFeedback();

        feedbackReplyRepository.delete(feedbackReply);

    }

    @Transactional
    public void update(Long feedbackReplyId, FeedbackReplyDTO.Request dto) {


        FeedbackReply feedbackReply = feedbackReplyRepository.findById(feedbackReplyId)
                .orElseThrow(() -> new ReplyNotFoundException("해당하는 답변을 찾을수 없습니다."));

        feedbackReply.update(dto.getContent(), dto.getAuthorName());
    }

    public List<FeedbackReplyDTO.Response> getFeedbackReplyList(Long feedbackId){

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException("해당 피드백을 찾을수 없습니다"));


        return feedbackReplyRepository.findByFeedbackIdOrderByCreatedAtDesc(feedbackId)
                .stream()
                .map(feedbackReply -> new FeedbackReplyDTO.Response(feedbackReply))
                .collect(Collectors.toList());
    }
}
