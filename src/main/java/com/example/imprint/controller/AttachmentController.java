package com.example.imprint.controller;

import com.example.imprint.domain.attachment.AttachmentEntity;
import com.example.imprint.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    /**
     * 파일 다운로드
     * GET /api/attachments/download/{attachmentId}
     */
    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) throws MalformedURLException {
        // 1. DB에서 파일 정보 조회
        AttachmentEntity attachment = attachmentService.getAttachmentEntity(attachmentId);

        // 2. 물리적 경로에서 파일 리소스로 변환
        String filePath = attachment.getFilePath();
        UrlResource resource = new UrlResource("file:" + filePath);

        // 3. 한글 파일명 깨짐 방지 인코딩
        String encodedFileName = UriUtils.encode(attachment.getFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}