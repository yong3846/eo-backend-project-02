package com.example.imprint.service.user;

import com.example.imprint.domain.user.EmailVerification;
import com.example.imprint.repository.user.EmailVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailVerificationRepository verificationRepository;

    @Test
    @DisplayName("인증 메일 발송 및 DB 저장 테스트")
    void sendCode_Success() {

        String email = "test@gmail.com";


        mailService.sendCodeToEmail(email);


        // 실제로 메일 전송 메서드가 호출되었는지 확인
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        // DB에 인증 정보가 저장되었는지 확인
        verify(verificationRepository, times(1)).save(any(EmailVerification.class));
    }

    @Test
    @DisplayName("인증 코드 검증 성공 테스트")
    void verifyCode_Success() {

        String email = "test@gmail.com";
        String code = "123456";
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .authCode(code)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        when(verificationRepository.findTopByEmailOrderByCreatedAtDesc(email))
                .thenReturn(Optional.of(verification));


        boolean result = mailService.verifyCode(email, code);


        assertTrue(result);
        // 엔티티의 상태가 true로 변했는지 확인
        assertTrue(verification.isVerified());
    }

    @Test
    @DisplayName("인증 코드 만료 시 실패 테스트")
    void verifyCode_Fail_Expired() {

        String email = "test@gmail.com";
        String code = "123456";
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .authCode(code)
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .build();

        when(verificationRepository.findTopByEmailOrderByCreatedAtDesc(email))
                .thenReturn(Optional.of(verification));

        // 만료되었을때
        assertThrows(IllegalArgumentException.class, () -> {
            mailService.verifyCode(email, code);
        }, "인증 시간이 만료되었습니다.");
    }
}