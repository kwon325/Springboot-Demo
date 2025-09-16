package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

      if (userRepository.count() == 0) {
        userService.signUp("user@example.com", "password123");

        userService.signUp("admin@example.com", "password123", List.of("ROLE_USER", "ROLE_ADMIN"));

        log.info("기본 사용자(user, admin) 생성이 완료되었습니다.");
      } else {
        log.info("이미 데이터가 존재하여 초기화를 건너뜁니다.");
      }
    };
  }
}