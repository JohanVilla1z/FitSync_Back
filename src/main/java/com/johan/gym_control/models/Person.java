package com.johan.gym_control.models;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Email(message = "El formato del correo electrónico no es válido")
  @NotBlank(message = "El correo electrónico no puede estar vacío")
  private String email;

  @Column(nullable = false)
  @NotBlank(message = "La contraseña no puede estar vacía")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
          message = "La contraseña debe contener al menos un número, una letra mayúscula, una minúscula y un carácter especial")
  private String password;

  @Column(nullable = false)
  @NotBlank(message = "El nombre completo no puede estar vacío")
  @Size(min = 2, max = 100, message = "El nombre completo debe tener entre 2 y 100 caracteres")
  private String fullName;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "El rol no puede estar vacío")
  private Role role;
}