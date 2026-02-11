package com.example.imprint.repository.user;

import com.example.imprint.domain.user.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    // 이메일로 가장 최근에 발송된 인증 정보 조회
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    // 이미 인증된 이메일인지 확인
    boolean existsByEmailAndIsVerifiedTrue(String email);
}
