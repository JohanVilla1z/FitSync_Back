package com.johan.gym_control.models.auth;

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
public class RegisterRequest {

  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
  private String name;

  @NotBlank(message = "El apellido no puede estar vacio")
  @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
  private String lastName;

  @NotBlank(message = "El email no puede estar vacio")
  @Email(message = "El formato del email no es valido")
  private String email;

  @NotBlank(message = "La contrasenia no puede estar vacia")
  @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
  private String password;
}