package com.example.imprint.controller;

import com.example.imprint.domain.post.PostRequestDto;
import com.example.imprint.domain.post.PostResponseDto;
import com.example.imprint.security.user.CustomUserDetails; // 인증 객체 임포트
import com.example.imprint.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 추가
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시물 등록 (로그인한 사용자 정보 자동 주입)
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> createPost(
            @Valid @RequestPart("post") PostRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // @RequestParam 제거

        // 세션에서 꺼낸 실제 유저 ID 사용
        Long postId = postService.createPost(requestDto, files, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    /**
     * 게시물 수정 (작성자 본인 확인 로직은 Service에서 수행)
     */
    @PutMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @Valid @RequestPart("post") PostRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        postService.updatePost(postId, requestDto, files, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        postService.deletePost(postId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시물 단건 상세 조회
     * GET /api/posts/{postId}
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 게시판의 게시물 목록 조회
     * GET /api/posts/board/{boardId}
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByBoard(@PathVariable Long boardId) {
        List<PostResponseDto> posts = postService.getPostsByBoard(boardId);
        return ResponseEntity.ok(posts);
    }
}
