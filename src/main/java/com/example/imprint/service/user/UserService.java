package com.example.imprint.service.user;

import com.example.imprint.domain.user.UserEntity;
import com.example.imprint.repository.user.EmailVerificationRepository;
import com.example.imprint.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.imprint.domain.user.UserJoinRequestDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository verificationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserJoinRequestDto request) {
        // 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        // 이메일 인증 여부 확인
        boolean isVerified = verificationRepository.existsByEmailAndIsVerifiedTrue(request.getEmail());
        if (!isVerified) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 엔티티 생성 및 상태 변경
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                // 비밀번호 암호화 후 저장
                .password(encodedPassword)
                .nickname(request.getNickname())
                .name(request.getName())
                .build();

        // 사용자 상태 전환
        user.activate();

        // 저장
        userRepository.save(user);
    }
}