package com.johan.gym_control.models.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
  @NotBlank(message = "El email no puede estar vacío")
  @Email(message = "El formato del email no es válido")
  private String email;

  @NotBlank(message = "La contraseña no puede estar vacía")
  private String password;
}