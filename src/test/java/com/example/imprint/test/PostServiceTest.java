package com.example.imprint.test;

import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.board.BoardRepository;
import com.example.imprint.domain.post.PostRequestDto;
import com.example.imprint.domain.post.PostResponseDto;
import com.example.imprint.domain.user.UserEntity;
import com.example.imprint.domain.user.UserRole;
import com.example.imprint.repository.user.UserRepository;
import com.example.imprint.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 후 DB 자동 롤백 (FK 에러 방지)
class PostServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private PostService postService;
    @Autowired private UserRepository userRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    private UserEntity testUser;
    private BoardEntity testBoard;

    @BeforeEach
    void setup() {
        // 1. 테스트용 유저 생성 및 저장
        testUser = userRepository.save(UserEntity.builder()
                .email("writer@test.com")
                .password(passwordEncoder.encode("password123"))
                .nickname("작성자")
                .name("김철수")
                .role(UserRole.USER)
                .status("ACTIVE")
                .build());

        // 2. 테스트용 게시판 생성 및 저장
        testBoard = boardRepository.save(BoardEntity.builder()
                .title("자유게시판")
                .build());
    }

    @Test
    @DisplayName("로그인된 사용자가 게시물을 작성하면 DB에 저장되어야 한다")
    @WithUserDetails(value = "writer@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createPostSuccess() throws Exception {
        // Given
        PostRequestDto requestDto = PostRequestDto.builder()
                .boardId(testBoard.getId())
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // When
        // 첨부파일 없이 등록하는 경우 (List<MultipartFile>은 null 전달)
        Long postId = postService.createPost(requestDto, null, testUser.getId());

        // Then
        PostResponseDto response = postService.getPost(postId);
        assertThat(response.getTitle()).isEqualTo("테스트 제목");
        assertThat(response.getNickname()).isEqualTo("작성자");
        assertThat(response.getBoardTitle()).isEqualTo("자유게시판");
    }

    @Test
    @DisplayName("존재하지 않는 게시판에 글을 쓰면 예외가 발생해야 한다")
    void createPostFailInvalidBoard() {
        PostRequestDto invalidDto = PostRequestDto.builder()
                .boardId(9999L) // 존재하지 않는 ID
                .title("에러 제목")
                .content("에러 내용")
                .build();

        assertThatThrownBy(() -> postService.createPost(invalidDto, null, testUser.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
