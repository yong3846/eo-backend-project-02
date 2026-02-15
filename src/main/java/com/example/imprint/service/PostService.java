package com.example.imprint.service;

import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.post.PostEntity;
import com.example.imprint.domain.post.PostResponseDto;
import com.example.imprint.domain.post.PostRequestDto;
import com.example.imprint.domain.user.UserEntity;
import com.example.imprint.domain.post.PostRepository;
import com.example.imprint.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final BoardService boardService;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService; // 1. 첨부파일 서비스 주입 추가

    /**
     * 게시물 등록 (파일 처리 로직 통합)
     */
    @Transactional
    public Long createPost(PostRequestDto requestDto, List<MultipartFile> files, Long userId) { // 매개변수 추가
        BoardEntity board = boardService.getBoardEntity(requestDto.getBoardId());
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. ID: " + userId));

        PostEntity post = PostEntity.builder()
                .board(board)
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        PostEntity savedPost = postRepository.save(post); // 2. 저장된 객체 확보

        // 3. 파일이 있다면 업로드 서비스 호출
        if (files != null && !files.isEmpty()) {
            attachmentService.uploadAttachments(savedPost, files);
        }

        return savedPost.getId();
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public void updatePost(Long postId, PostRequestDto requestDto, List<MultipartFile> files, Long userId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. ID: " + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("해당 게시물을 수정할 권한이 없습니다.");
        }

        post.update(requestDto.getTitle(), requestDto.getContent());

        // 4. 수정 시 추가된 파일 처리 (기존 요구사항 반영)
        if (files != null && !files.isEmpty()) {
            attachmentService.uploadAttachments(post, files);
        }
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. ID: " + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("해당 게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    /**
     * 게시물 단건 상세 조회
     */
    public PostResponseDto getPost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. ID: " + postId));

        return new PostResponseDto(post);
    }

    /**
     * 특정 게시판의 전체 게시물 조회
     */
    public List<PostResponseDto> getPostsByBoard(Long boardId) {
        // 게시판이 존재하는지 먼저 확인 (Optional)
        boardService.getBoardEntity(boardId);

        return postRepository.findByBoardId(boardId).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }
}
