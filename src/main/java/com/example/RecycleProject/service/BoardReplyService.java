package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.BoardReplyDTO;
import com.example.RecycleProject.ENUM.BoardStatus;
import com.example.RecycleProject.Repository.BoardReplyRepository;
import com.example.RecycleProject.Repository.BoardRepository;
import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.BoardReply;
import com.example.RecycleProject.exception.BoardNotFoundException;
import com.example.RecycleProject.exception.ReplyNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
/*
    1. 답변 등록
    2. 답변 삭제
    3. 답변 수정
    4. 답변 조회(최신순)
 */
public class BoardReplyService {

    private  final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;

    @Transactional
    public Long registerBoardReply(Long boardId, BoardReplyDTO.Request dto){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을수 없습니다."));

        BoardReply entity = dto.toEntity(board);
        BoardReply saveBoardReply = boardReplyRepository.save(entity);
        return saveBoardReply.getId();
    }

    @Transactional
    public void updateBoardReply(Long boardReplyId, BoardReplyDTO.Request dto){

        BoardReply boardReply = boardReplyRepository.findById(boardReplyId)
                .orElseThrow(() -> new ReplyNotFoundException("해당 답변을 찾을수 없습니다."));

        boardReply.update(dto.getReplyContent(), dto.getAuthorName());

    }

    @Transactional
    public void deleteBoardReply(Long boardReplyId) {
        BoardReply boardReply = boardReplyRepository.findById(boardReplyId)
                .orElseThrow(() -> new ReplyNotFoundException("해당 답변을 찾을수 없습니다."));

        Board board = boardReply.getBoard();

        boardReplyRepository.delete(boardReply);

        if (board.getBoardReplyList().size() <= 1) { // 현재 트랜잭션 내 삭제 반영 확인 필요
            board.updateStatus(BoardStatus.WAITING);
        }

    }

    // 답변 조회 목록(최신순으로)
    public List<BoardReplyDTO.Response> getBoardReplyList(Long boardId) {


        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을수 없습니다"));

        return boardReplyRepository.findByBoardIdOrderByCreatedAtDesc(boardId)
                .stream()
                .map(dto -> new BoardReplyDTO.Response(dto))
                .collect(Collectors.toList());
    }
}
