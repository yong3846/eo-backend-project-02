package com.example.imprint.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostRequestDto {

    @NotNull(message = "게시판 ID는 필수입니다.")
    private Long boardId;

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(max = 200, message = "제목은 200자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;
}
