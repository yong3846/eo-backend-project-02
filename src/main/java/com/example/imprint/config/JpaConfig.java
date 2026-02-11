package com.example.imprint.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// 생성/수정 시간 및 생성/수정자 정보를 자동으로 관리해주는 기능
@EnableJpaAuditing
public class JpaConfig {
}
