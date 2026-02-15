package com.example.imprint.service;

import com.example.imprint.domain.attachment.AttachmentEntity;
import com.example.imprint.domain.attachment.AttachmentRepository;
import com.example.imprint.domain.post.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${file.dir}") // application.properties에 설정된 경로 사용
    private String fileDir;

    /**
     * 다중 파일 업로드 및 DB 저장
     */
    @Transactional
    public void uploadAttachments(PostEntity post, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 1. 서버에 물리적 파일 저장
                    String originalFilename = file.getOriginalFilename();
                    String storeFilename = createStoreFileName(originalFilename);
                    String fullPath = fileDir + storeFilename;
                    file.transferTo(new File(fullPath));

                    // 2. DB에 메타데이터 저장
                    AttachmentEntity attachment = AttachmentEntity.builder()
                            .fileName(originalFilename) // 원본명
                            .filePath(fullPath)         // 저장경로
                            .fileType(file.getContentType())
                            .board(post.getBoard())
                            .post(post)
                            .user(post.getUser())
                            .build();

                    attachmentRepository.save(attachment);
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
                }
            }
        }
    }

    // 파일명 중복 방지를 위한 UUID 생성 (예: uuid.png)
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자 추출 (예: png)
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public AttachmentEntity getAttachmentEntity(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 첨부파일이 존재하지 않습니다."));
    }

    /**
     * 테스트 및 직접 저장용 메서드
     */
    @Transactional
    public void saveAttachment(PostEntity post, String fileName, String path, String type) {
        AttachmentEntity attachment = AttachmentEntity.builder()
                .fileName(fileName)
                .filePath(path)
                .fileType(type)
                .board(post.getBoard())
                .post(post)
                .user(post.getUser())
                .build();

        attachmentRepository.save(attachment);
    }
}
