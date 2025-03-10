package com.johan.gym_control.models.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRequestDTO {

  @Size(min = 2, max = 100, message = "El nombre del equipo debe tener entre 2 y 100 caracteres")
  @NotBlank(message = "El nombre del equipo no puede estar vacio")
  private String name;

  @Size(max = 255, message = "La descripcion del equipo no puede tener más de 255 caracteres")
  private String description;

  @NotNull(message = "La disponibilidad del equipo no puede estar vacia")
  private Boolean available;
}