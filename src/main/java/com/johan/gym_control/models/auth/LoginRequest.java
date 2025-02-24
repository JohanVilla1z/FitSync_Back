package com.johan.gym_control.models.auth;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}