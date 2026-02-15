package com.example.imprint.domain.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {

    @NotBlank(message = "게시판 제목은 필수입니다.")
    @Size(max = 50, message = "게시판 제목은 50자 이내여야 합니다.")
    private String title;

    // 생성자 (테스트 코드 등에서 사용하기 위함)
    public BoardRequestDto(String title) {
        this.title = title;
    }
}
