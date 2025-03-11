package com.johan.gym_control.controllers.auth;

import com.johan.gym_control.models.auth.AdminRequest;
import com.johan.gym_control.models.auth.LoginRequest;
import com.johan.gym_control.models.auth.LoginResponse;
import com.johan.gym_control.models.auth.RegisterRequest;
import com.johan.gym_control.models.auth.RegisterResponse;
import com.johan.gym_control.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.authenticate(loginRequest));
  }

  @PostMapping("/register-user")
  public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
  }

  @PostMapping("/register-admin")
  public ResponseEntity<RegisterResponse> registerAdmin(@Valid @RequestBody AdminRequest adminRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdmin(adminRequest));
  }
}