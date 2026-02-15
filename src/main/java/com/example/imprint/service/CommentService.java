package com.example.imprint.service;

import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.comment.CommentEntity;
import com.example.imprint.domain.comment.CommentRequestDto;
import com.example.imprint.domain.comment.CommentResponseDto;
import com.example.imprint.domain.post.PostEntity;
import com.example.imprint.domain.user.UserEntity;
import com.example.imprint.domain.comment.CommentRepository;
import com.example.imprint.domain.post.PostRepository;
import com.example.imprint.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardService boardService; // 기존에 만든 보드 서비스 활용

    @Transactional
    public Long createComment(CommentRequestDto requestDto, Long userId) {
        // 1. 게시물 존재 여부 확인 (필수 요구사항)
        PostEntity post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. ID: " + requestDto.getPostId()));

        // 2. 게시판 및 사용자 조회
        BoardEntity board = boardService.getBoardEntity(requestDto.getBoardId());
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + userId));

        // 3. 댓글 생성 및 저장
        CommentEntity comment = CommentEntity.builder()
                .content(requestDto.getContent())
                .board(board)
                .post(post)
                .user(user)
                .build();

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void updateComment(Long commentId, String content, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        comment.update(content);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    /**
     * 특정 게시물의 모든 댓글 조회
     */
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByIdAsc(postId).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
