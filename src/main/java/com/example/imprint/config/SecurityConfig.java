package com.example.imprint.config;

import com.example.imprint.security.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    // 비밀번호 암호화 빈 등록 (로그인 시 비밀번호 비교에 사용됨)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(csrf -> csrf.disable()) // POST 요청 허용을 위해 필수
                .headers(headers -> headers.frameOptions(f -> f.disable()))

                .authorizeHttpRequests(auth -> auth
                        // API 주소를 명시
                        .requestMatchers("/api/mail/send", "/api/mail/verify", "/api/user/join").permitAll()

                        // 화면 주소들도 명시
                        .requestMatchers("/test.html", "/loginForm", "/joinForm", "/").permitAll()

                        // 나머지만 잠그기
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .usernameParameter("email")
                        .permitAll()
                )


                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
}