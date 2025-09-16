package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitConfig {

    private final UserService userService;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("데이터 초기화를 시작합니다...");

            // 이미 사용자가 있으면 생성하지 않음
            if (userRepository.count() == 0) {
                // 일반 사용자 생성
                userService.signUp("user@example.com", "password123");

                // 관리자 생성
                userService.signUp("admin@example.com", "password123", List.of("ROLE_USER", "ROLE_ADMIN"));

                log.info("기본 사용자(user, admin) 생성이 완료되었습니다.");
            } else {
                log.info("이미 데이터가 존재하여 초기화를 건너뜁니다.");
            }
        };
    }
}