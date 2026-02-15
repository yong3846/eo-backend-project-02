package com.example.imprint.domain.post;

import com.example.imprint.domain.BaseTimeEntity;
import com.example.imprint.domain.attachment.AttachmentEntity;
import com.example.imprint.domain.board.BoardEntity;
import com.example.imprint.domain.comment.CommentEntity;
import com.example.imprint.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 게시판에 속해 있는가 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    // 누가 작성했는가 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // --- 비즈니스 로직 ---

    /**
     * 게시물 수정
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc") // 댓글 정렬 순서 보장
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttachmentEntity> attachments = new ArrayList<>();
}
