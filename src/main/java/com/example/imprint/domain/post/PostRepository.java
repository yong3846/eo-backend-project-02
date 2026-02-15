package com.example.imprint.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // 특정 게시판의 게시물 목록 조회 (페이징 처리 등을 고려할 수 있음)
    List<PostEntity> findByBoardId(Long boardId);
}