package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.request.UserSignUpRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "사용자 회원가입 및 로그인 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/signup")
  public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequest request) {
    userService.signUp(request.getEmail(), request.getPassword());
    return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
  }

  @PostMapping("/signin")
  public ResponseEntity<TokenResponse> signIn(
      @RequestParam("username") String email,
      @RequestParam("password") String password
  ) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtTokenProvider.createToken(userDetails);

    return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
  }
}