package com.example.imprint.controller.user;

import com.example.imprint.domain.user.UserJoinRequestDto;
import com.example.imprint.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody @Valid UserJoinRequestDto requestDto) {
        userService.registerUser(requestDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }
}