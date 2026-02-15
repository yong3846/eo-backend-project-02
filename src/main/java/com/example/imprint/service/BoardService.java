package com.example.imprint.service;

import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.board.BoardResponseDto;
import com.example.imprint.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시판 엔티티 조회 (내부 서비스용)
     */
    public BoardEntity getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 존재하지 않습니다. ID: " + boardId));
    }

    /**
     * 게시판 생성 (관리자용)
     */
    @Transactional
    public Long createBoard(String title) {
        BoardEntity board = BoardEntity.builder()
                .title(title)
                .build();
        return boardRepository.save(board).getId();
    }

    /**
     * 전체 게시판 목록 조회 (네비게이션 바 등에서 사용)
     */
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }
}