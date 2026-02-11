package com.example.imprint.controller.user;

import com.example.imprint.service.user.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    // 발송
    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestParam String email) {
        mailService.sendCodeToEmail(email);
        return ResponseEntity.ok("인증번호가 이메일로 발송되었습니다.");
    }

    // 확인
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = mailService.verifyCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
        }
    }
}