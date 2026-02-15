package com.example.imprint.domain.post;

import com.example.imprint.domain.post.PostEntity;
import com.example.imprint.domain.comment.CommentResponseDto;
import com.example.imprint.domain.attachment.AttachmentResponseDto; // 추가
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long boardId;
    private String boardTitle;
    private Long userId;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponseDto> comments;
    private List<AttachmentResponseDto> attachments; // 추가: 첨부파일 목록

    public PostResponseDto(PostEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.boardId = entity.getBoard().getId();
        this.boardTitle = entity.getBoard().getTitle();
        this.userId = entity.getUser().getId();
        this.nickname = entity.getUser().getNickname();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();

        // 댓글 변환
        if (entity.getComments() != null) {
            this.comments = entity.getComments().stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }

        // 추가: 첨부파일 변환
        // PostEntity에 @OneToMany(mappedBy = "post") 연관관계가 설정되어 있어야 합니다.
        if (entity.getAttachments() != null) {
            this.attachments = entity.getAttachments().stream()
                    .map(AttachmentResponseDto::new)
                    .collect(Collectors.toList());
        }
    }
}
