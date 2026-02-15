package com.example.imprint.test;

import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.post.PostEntity;
import com.example.imprint.domain.post.PostRequestDto;
import com.example.imprint.domain.post.PostResponseDto;
import com.example.imprint.domain.comment.CommentRequestDto;
import com.example.imprint.domain.user.UserEntity;
import com.example.imprint.domain.user.UserRole;
import com.example.imprint.domain.board.BoardRepository;
import com.example.imprint.domain.post.PostRepository;
import com.example.imprint.repository.user.UserRepository;
import com.example.imprint.service.AttachmentService;
import com.example.imprint.service.CommentService;
import com.example.imprint.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 필수: AssertJ static import (isEqualTo, hasSize 등을 사용하기 위함)
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 완료 후 자동 롤백 (FK 제약조건 에러 방지)
class PostServiceDetailTest {

    @Autowired private PostService postService;
    @Autowired private CommentService commentService;
    @Autowired private AttachmentService attachmentService;
    @Autowired private UserRepository userRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private PostRepository postRepository;

    private UserEntity user;
    private PostEntity post;
    private BoardEntity board;

    @Autowired
    private jakarta.persistence.EntityManager em; // 엔티티 매니저 주입

    @BeforeEach
    void setup() {
        // 1. 테스트용 유저 생성 (필수 필드 포함)
        user = userRepository.save(UserEntity.builder()
                .email("detail@test.com")
                .password("password123")
                .nickname("상세조회자")
                .name("홍길동")
                .role(UserRole.USER)
                .status("ACTIVE")
                .build());

        // 2. 테스트용 게시판 생성 (name 대신 title 필드 사용)
        board = boardRepository.save(BoardEntity.builder()
                .title("자유게시판")
                .build());

        // 3. 테스트용 게시물 생성
        post = postRepository.save(PostEntity.builder()
                .board(board)
                .user(user)
                .title("상세조회 테스트 제목")
                .content("상세조회 테스트 본문")
                .build());

        // 4. 댓글 2개 추가
        commentService.createComment(CommentRequestDto.builder()
                .boardId(board.getId())
                .postId(post.getId())
                .content("첫 번째 댓글")
                .build(), user.getId());

        commentService.createComment(CommentRequestDto.builder()
                .boardId(board.getId())
                .postId(post.getId())
                .content("두 번째 댓글")
                .build(), user.getId());

        // 5. 가짜 첨부파일 1개 추가 (DB 기록용)
        attachmentService.saveAttachment(post, "test_image.png", "/upload/test_image.png", "image/png");

        em.flush(); // DB에 반영
        em.clear(); // 영속성 컨텍스트 비우기
    }

    @Test
    @DisplayName("게시물 상세 조회 시 작성자 정보, 댓글 목록, 첨부파일 목록이 모두 포함되어야 한다")
    void getPostDetailSuccess() {
        // When
        PostResponseDto result = postService.getPost(post.getId());

        // Then
        assertThat(result.getTitle()).isEqualTo("상세조회 테스트 제목");
        assertThat(result.getNickname()).isEqualTo("상세조회자");

        // 댓글 검증
        assertThat(result.getComments()).hasSize(2);
        assertThat(result.getComments().get(0).getContent()).isEqualTo("첫 번째 댓글");

        // 첨부파일 검증
        assertThat(result.getAttachments()).hasSize(1);
        assertThat(result.getAttachments().get(0).getFileName()).isEqualTo("test_image.png");
    }
}
