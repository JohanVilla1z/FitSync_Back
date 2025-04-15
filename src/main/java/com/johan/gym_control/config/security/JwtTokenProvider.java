package com.johan.gym_control.config.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpiration;

  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(UserDetails userDetails) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(getSigningKey())
        .addClaims(Map.of("role", userDetails.getAuthorities().iterator().next().getAuthority()))
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return getClaimsFromToken(token).getSubject();
  }

  public Claims getClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}