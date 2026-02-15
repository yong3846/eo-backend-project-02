package com.example.imprint.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // 특정 게시물의 댓글 목록을 작성순(ID순)으로 조회
    List<CommentEntity> findByPostIdOrderByIdAsc(Long postId);
}
