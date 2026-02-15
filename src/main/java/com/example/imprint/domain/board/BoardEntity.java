package com.example.imprint.domain.board;

import com.example.imprint.domain.BaseTimeEntity;
import com.example.imprint.domain.post.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title; // 게시판 이름 (예: 자유게시판, 공지사항)

    // 게시판에 속한 게시물 목록 (양방향 매핑이 필요한 경우)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostEntity> posts = new ArrayList<>();
}
