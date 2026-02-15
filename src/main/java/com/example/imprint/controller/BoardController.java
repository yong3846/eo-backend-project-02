package com.example.imprint.controller;

import com.example.imprint.domain.board.BoardRequestDto;
import com.example.imprint.domain.board.BoardResponseDto;
import com.example.imprint.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 새로운 게시판 생성 (관리자용 기능)
     * POST /api/boards
     */
    @PostMapping
    public ResponseEntity<Long> createBoard(@RequestBody BoardRequestDto requestDto) {
        // 제목만 넘기는 간단한 구조라면 requestDto.getTitle() 사용
        Long boardId = boardService.createBoard(requestDto.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(boardId);
    }

    /**
     * 전체 게시판 목록 조회 (네비게이션 메뉴 등에서 사용)
     * GET /api/boards
     */
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> boards = boardService.getAllBoards();
        return ResponseEntity.ok(boards);
    }
}
