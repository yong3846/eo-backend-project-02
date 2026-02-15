package com.example.imprint.domain.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String nickname; // 작성자 닉네임
    private Long userId;     // 수정/삭제 버튼 노출 여부 판단용
    private LocalDateTime createdAt;

    /**
     * Entity -> DTO 변환 생성자
     */
    public CommentResponseDto(CommentEntity entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.nickname = entity.getUser().getNickname();
        this.userId = entity.getUser().getId();
        this.createdAt = entity.getCreatedAt();
    }
}
