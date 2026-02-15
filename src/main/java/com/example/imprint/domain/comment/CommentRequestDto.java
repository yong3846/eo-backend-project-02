package com.example.imprint.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentRequestDto {

    @NotNull(message = "게시판 ID는 필수입니다.")
    private Long boardId;

    @NotNull(message = "게시물 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
