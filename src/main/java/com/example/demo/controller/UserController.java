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
@PreAuthorize("hasRole('USER')")
public class UserController {

  @GetMapping("/me")
  public ResponseEntity<String> getMyProfile() {
    return ResponseEntity.ok("내 프로필 정보입니다.");
  }

  @GetMapping("/{userId}")
  public ResponseEntity<String> getUserProfileById(@PathVariable Long userId) {
    return ResponseEntity.ok("사용자 ID " + userId + "의 프로필 정보입니다.");
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
    return ResponseEntity.ok("사용자 " + userId + "가 삭제되었습니다.");
  }
}