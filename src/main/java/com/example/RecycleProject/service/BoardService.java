package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.BoardDTO;
import com.example.RecycleProject.Repository.BoardRepository;
import com.example.RecycleProject.Repository.UserRepository;
import com.example.RecycleProject.domain.Board;
import com.example.RecycleProject.domain.User;
import com.example.RecycleProject.exception.AccessDeniedException;
import com.example.RecycleProject.exception.BoardNotFoundException;
import com.example.RecycleProject.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    게시물 저장, 수정, 삭제, 조회(제목을 통해서), 특정유저의 길을 최신순으로
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //write 작성시에 저장
    /*
        유저를 찾고 이에 맞게 저장하자
     */
    @Transactional
    public Long write(BoardDTO.Request dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("해당 아이디의 사용자가 존재하지 않습니다."));

        Board entity = dto.toEntity(user);

        Board saveBoard = boardRepository.save(entity);

        return saveBoard.getId();
    }

    @Transactional
    public void updateBoard(Long boardId, Long userId, BoardDTO.Request dto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("수정할 게시물이 존재하지 않습니다."));

        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        board.updateBoard(dto.getTitle(), dto.getContent());

        // dirty check 으로 자동으로 반영이 된다.
    }

    @Transactional
    public void deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("삭제할 게시물이 존재하지 않습니다."));

        boardRepository.delete(board);
    }

    //단건조회, 모두 조회(특정 유저의 것만 최신순으로), 제목으로 조회,

    public BoardDTO.Response findOneBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("조회할 게시물이 없습니다."));

        return new BoardDTO.Response(board);
    }

    // 다건 조회

    public List<BoardDTO.Response> findAllByUserDESC(Long userId){

        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을수 없습니다."));

        List<Board> byUserOrderByCreatedAtDesc =
                boardRepository.findByUserOrderByCreatedAtDesc(user);

        List<BoardDTO.Response> dtoList = new ArrayList<>();

        for (Board response : byUserOrderByCreatedAtDesc) {
            BoardDTO.Response result = new BoardDTO.Response(response);
            dtoList.add(result);
        }

        return dtoList;
    }

    // 제목으로 조회
    public List<BoardDTO.Response> searchByName(Long userId, String title){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을수 없습니다."));

        List<Board> myBoards = boardRepository.findByUserAndTitleContainingOrderByCreatedAtDesc(user, title);

        //dto로 변환
        return myBoards.stream()
                .map(board -> new BoardDTO.Response(board))
                .toList();
    }




}
