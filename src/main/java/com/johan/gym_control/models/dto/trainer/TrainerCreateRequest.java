package com.johan.gym_control.models.dto.trainer;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateRequest {
  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
  private String name;

  @NotBlank(message = "El email no puede estar vacío")
  @Email(message = "El formato del email no es válido")
  private String email;

  @NotBlank(message = "La contraseña no puede estar vacía")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;

  private Boolean isActive = true;
}