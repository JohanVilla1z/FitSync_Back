package com.johan.gym_control.models.auth;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
  private String token;
  private String type = "Bearer";
  private String email;
  private String role;
}