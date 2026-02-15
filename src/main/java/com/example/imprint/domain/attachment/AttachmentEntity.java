package com.example.imprint.domain.attachment;

import com.example.imprint.domain.BaseTimeEntity;
import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.post.PostEntity;
import com.example.imprint.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AttachmentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // 원본 파일명

    @Column(nullable = false)
    private String filePath; // 서버 내 저장 경로

    @Column(nullable = false)
    private String fileType; // 파일 형식 (MIME type 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
