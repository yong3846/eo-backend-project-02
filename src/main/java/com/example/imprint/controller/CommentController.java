package com.example.imprint.controller;

import com.example.imprint.domain.comment.CommentRequestDto;
import com.example.imprint.domain.comment.CommentResponseDto;
import com.example.imprint.security.user.CustomUserDetails;
import com.example.imprint.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     * 인증된 세션에서 userId를 자동으로 추출합니다.
     */
    @PostMapping
    public ResponseEntity<Long> createComment(
            @Valid @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long commentId = commentService.createComment(requestDto, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.updateComment(commentId, content, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.deleteComment(commentId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 게시물의 댓글 목록 조회 (인증 불필요)
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }
}
