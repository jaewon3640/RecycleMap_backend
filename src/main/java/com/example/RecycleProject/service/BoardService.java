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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void updateBoard(Long boardId, Long userId, BoardDTO.Request dto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("수정할 게시물이 존재하지 않습니다."));

        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        board.updateBoard(dto.getTitle(), dto.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("삭제할 게시물이 존재하지 않습니다."));

        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
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

    public Page<BoardDTO.Response> findAll(Pageable pageable) {
        return boardRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(BoardDTO.Response::new);
    }

    public Page<BoardDTO.Response> search(String title, Pageable pageable) {
        return boardRepository.findByTitleContainingOrderByCreatedAtDesc(title, pageable)
                .map(BoardDTO.Response::new);
    }

    // [커서 페이징] lastId 다음(작은 id)부터 size개. lastId 없으면 최신부터(MAX_VALUE).
    //   응답이 size보다 적게 오면 마지막 페이지. 클라이언트는 마지막 항목 id를 다음 lastId로 사용.
    public List<BoardDTO.Response> findWithCursor(Long lastId, int size) {
        Long cursor = (lastId == null) ? Long.MAX_VALUE : lastId;
        return boardRepository.findByIdLessThanOrderByIdDesc(cursor, PageRequest.of(0, size))
                .stream()
                .map(BoardDTO.Response::new)
                .toList();
    }
}
