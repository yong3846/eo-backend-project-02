package com.example.imprint.domain.user;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum UserRole {
    USER(0, "일반 사용자"),
    MANAGER(1, "매니저"),
    ADMIN(2, "관리자");

    private final int value;
    private final String description;

    UserRole(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static UserRole fromValue(int value) {
        return Arrays.stream(UserRole.values())
                .filter(v -> v.getValue() == value)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 권한: " + value));
    }
}