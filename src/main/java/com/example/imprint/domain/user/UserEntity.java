package com.example.imprint.domain.user;


import com.example.imprint.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UserRole role;

    // PENDING, ACTIVE, BANNED
    private String status = "PENDING";


    @Builder
    public UserEntity(String email, String password, String nickname, String name) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;

        this.role = UserRole.USER;
        this.status = "PENDING";
    }

    // 비즈니스 로직
    public void activate() {
        this.status = "ACTIVE";
    }

    public void changeRole(UserRole newRole) {
        this.role = newRole;
    }
}