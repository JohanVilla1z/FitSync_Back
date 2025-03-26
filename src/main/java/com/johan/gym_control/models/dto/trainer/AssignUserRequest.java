package com.johan.gym_control.models.dto.trainer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignUserRequest {
  @NotNull(message = "El ID del usuario no puede estar vacío")
  private Long userId;

  @NotNull(message = "El ID del entrenador no puede estar vacío")
  private Long trainerId;
}