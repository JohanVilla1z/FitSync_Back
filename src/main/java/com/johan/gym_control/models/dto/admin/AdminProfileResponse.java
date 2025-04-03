package com.johan.gym_control.models.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con la información del perfil de un administrador")
public class AdminProfileResponse {

  @Schema(description = "ID del administrador", example = "1")
  private Long id;

  @Schema(description = "Nombre completo del administrador", example = "Juan Pérez")
  private String name;

  @Schema(description = "Correo electrónico del administrador", example = "admin@example.com")
  private String email;
}