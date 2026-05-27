package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.BoardDTO;
import com.example.RecycleProject.service.BoardService;
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
@RequestMapping("/api/board")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<Long> write(@RequestBody @Valid BoardDTO.Request request,
                                      @AuthenticationPrincipal Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(boardService.write(request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO.Response> getBoard(@PathVariable Long id) {
        log.info("게시글 상세 조회 요청 - ID: {}", id);
        BoardDTO.Response oneBoard = boardService.findOneBoard(id);
        log.info("조회된 게시글: Title={}, Author={}", oneBoard.getTitle(), oneBoard.getAuthorName());
        return ResponseEntity.ok(oneBoard);
    }

    @GetMapping("/search-name")
    public ResponseEntity<Page<BoardDTO.Response>> search(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<BoardDTO.Response> result = boardService.findAllorSearch(title, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(
            @PathVariable Long boardId, String email,
            @RequestBody BoardDTO.Request request) {
        boardService.updateBoard(boardId, email, request);
        return ResponseEntity.ok().build();
    }
}
