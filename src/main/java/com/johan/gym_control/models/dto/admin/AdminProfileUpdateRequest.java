package com.johan.gym_control.models.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar el perfil de un administrador")
public class AdminProfileUpdateRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Schema(description = "Nombre completo del administrador", example = "Juan Pérez")
  private String name;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Schema(description = "Correo electrónico del administrador", example = "admin@example.com")
  private String email;
}