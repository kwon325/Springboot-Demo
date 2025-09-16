package com.example.demo.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j // Logger 사용을 위한 어노테이션
public class JwtTokenProvider {

  private final SecretKey key;
  private final long validityInMilliseconds;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
      @Value("${jwt.expiration_ms}") long validityInMilliseconds) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.validityInMilliseconds = validityInMilliseconds;
  }

  public String createToken(UserDetails userDetails) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("roles", roles)
        .issuedAt(now)
        .expiration(validity)
        .signWith(key)
        .compact();
  }

  public String getUsername(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public Collection<? extends GrantedAuthority> getAuthorities(String token) {
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    @SuppressWarnings("unchecked")
    List<String> roles = claims.get("roles", List.class);
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (io.jsonwebtoken.UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }
}