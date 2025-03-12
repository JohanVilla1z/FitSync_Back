package com.johan.gym_control.models.dto.user;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
  private String name;

  @NotBlank(message = "El apellido no puede estar vacío")
  @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
  private String lastName;

  @NotBlank(message = "El email no puede estar vacío")
  @Email(message = "El formato del email no es válido")
  private String email;

  @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
  private String phone;

  @Positive(message = "El peso debe ser un valor positivo")
  @DecimalMax(value = "300.0", message = "El peso no puede ser mayor a 300 kg")
  @DecimalMin(value = "20.0", message = "El peso no puede ser menor a 20 kg")
  private Float weight;

  @Positive(message = "La altura debe ser un valor positivo")
  @DecimalMax(value = "2.5", message = "La altura no puede ser mayor a 2.5 metros")
  @DecimalMin(value = "0.5", message = "La altura no puede ser menor a 0.5 metros")
  private Float height;
}