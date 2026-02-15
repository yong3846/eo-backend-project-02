package com.example.imprint.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;

    /**
     * Entity -> DTO 변환 생성자
     * 서비스 계층에서 .map(BoardResponseDto::new)로 간결하게 사용 가능합니다.
     */
    public BoardResponseDto(BoardEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
    }
}
