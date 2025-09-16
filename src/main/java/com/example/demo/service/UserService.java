package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 회원가입 메서드
   *
   * @param email    사용자 이메일
   * @param password 사용자 비밀번호
   * @return 저장된 사용자 정보
   */
  public User signUp(String email, String password) {
    return signUp(email, password, Collections.singletonList("ROLE_USER"));
  }

  public User signUp(String email, String password, List<String> roles) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }

    User user = User.builder()
        .email(email)
        .password(passwordEncoder.encode(password))
        .roles(roles)
        .build();

    return userRepository.save(user);
  }

  /**
   * @param username the username identifying the user whose data is required.
   * @return a fully populated user record (never `null`)
   * @throws UsernameNotFoundException if the user could not be found or the user has no
   *                                   GrantedAuthority
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
  }
}