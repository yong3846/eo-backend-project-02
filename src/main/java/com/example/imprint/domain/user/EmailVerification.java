package com.example.imprint.domain.user;

import com.example.imprint.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String authCode;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Builder
    public EmailVerification(String email, String authCode, LocalDateTime expiresAt) {
        this.email = email;
        this.authCode = authCode;
        this.expiresAt = expiresAt;
        this.isVerified = false;
    }

    // 인증 완료 시 상태 변경 로직
    public void verify() {
        this.isVerified = true;
    }
}
