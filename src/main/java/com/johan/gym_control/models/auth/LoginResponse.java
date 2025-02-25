package com.johan.gym_control.models.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private String token;
  private String type = "Bearer";
  private String email;
  private String role;
}