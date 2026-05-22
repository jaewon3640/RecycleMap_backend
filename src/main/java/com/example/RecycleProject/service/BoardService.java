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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long write(BoardDTO.Request dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 아이디의 사용자가 존재하지 않습니다."));

        Board saveBoard = boardRepository.save(dto.toEntity(user));
        return saveBoard.getId();
    }

    @Transactional
    public void updateBoard(Long boardId, String email, BoardDTO.Request dto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("수정할 게시물이 존재하지 않습니다."));

        if (!board.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        board.updateBoard(dto.getTitle(), dto.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("삭제할 게시물이 존재하지 않습니다."));

        boardRepository.delete(board);
    }

    public BoardDTO.Response findOneBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("조회할 게시물이 없습니다."));

        return new BoardDTO.Response(board);
    }

    public Page<BoardDTO.Response> findAllByUserDESC(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을수 없습니다."));

        return boardRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                .map(BoardDTO.Response::new);
    }

    public Page<BoardDTO.Response> findAllorSearch(String title, Pageable pageable) {
        if (title == null || title.trim().isEmpty()) {
            return boardRepository.findAllByOrderByCreatedAtDesc(pageable)
                    .map(BoardDTO.Response::new);
        }
        return boardRepository.findByTitleContainingOrderByCreatedAtDesc(title, pageable)
                .map(BoardDTO.Response::new);
    }
}
