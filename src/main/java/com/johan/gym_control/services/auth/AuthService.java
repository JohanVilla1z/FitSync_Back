package com.johan.gym_control.services.auth;

import com.johan.gym_control.config.security.JwtTokenProvider;
import com.johan.gym_control.exceptions.auth.AuthenticationException;
import com.johan.gym_control.models.auth.LoginRequest;
import com.johan.gym_control.models.auth.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final CustomUserDetailsService userDetailsService;

  public LoginResponse authenticate(LoginRequest request) {
    try {
      // Validate user exists before authentication
      UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

      // Authenticate credentials
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()));

      // Generate JWT token
      String jwt = tokenProvider.generateToken(userDetails);

      // Build and return response
      return LoginResponse.builder()
          .token(jwt)
          .type("Bearer")
          .email(userDetails.getUsername())
          .role(userDetails.getAuthorities().iterator().next().getAuthority())
          .build();

    } catch (AuthenticationException e) {
      throw new com.johan.gym_control.exceptions.auth.AuthenticationException(
          "Credenciales inválidas: " + e.getMessage());
    } catch (Exception e) {
      throw new com.johan.gym_control.exceptions.auth.AuthenticationException(
          "Error en la autenticación: " + e.getMessage());
    }
  }
}