package com.johan.gym_control.controllers.auth;

import com.johan.gym_control.models.auth.LoginRequest;
import com.johan.gym_control.models.auth.LoginResponse;
import com.johan.gym_control.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.authenticate(loginRequest));
  }
}