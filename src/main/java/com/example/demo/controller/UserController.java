package com.example.demo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 정보 API", description = "사용자 정보 조회 관련 API")
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')") // 이 컨트롤러의 모든 API는 최소 'USER' 역할이 필요
public class UserController {

  @GetMapping("/me")
  public ResponseEntity<String> getMyProfile() {
    // 이 API는 클래스의 @PreAuthorize("hasRole('USER')") 규칙을 그대로 상속받습니다.
    // 따라서 'ROLE_USER' 또는 'ROLE_ADMIN' 역할을 가진 사용자만 접근 가능합니다.
    return ResponseEntity.ok("내 프로필 정보입니다.");
  }

  @GetMapping("/{userId}")
  public ResponseEntity<String> getUserProfileById(@PathVariable Long userId) {
    // 이 API도 클래스의 규칙을 상속받아 'ROLE_USER' 역할이 필요합니다.
    return ResponseEntity.ok("사용자 ID " + userId + "의 프로필 정보입니다.");
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')") // 메서드 레벨에서 더 강력한 권한으로 덮어쓰기(Override)
  public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
    // 이 API는 클래스 규칙을 무시하고, 오직 'ROLE_ADMIN' 역할을 가진 사용자만 접근 가능합니다.
    return ResponseEntity.ok("사용자 " + userId + "가 삭제되었습니다.");
  }
}