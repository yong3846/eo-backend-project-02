package com.example.imprint.domain.attachment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttachmentResponseDto {
    private Long id;
    private String fileName;

    public AttachmentResponseDto(AttachmentEntity entity) {
        this.id = entity.getId();
        this.fileName = entity.getFileName();
    }
}