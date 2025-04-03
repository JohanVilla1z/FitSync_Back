package com.johan.gym_control.models.dto.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado para información de usuario
 * Evita referencias circulares eliminando la referencia al entrenador
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleDTO {
  private Long id;
  private String name;
  private String email;
  private String role;
  private String userLastName;
  private Boolean isActive;
  private Date registerDate;
  private String userPhone;
  private Float userWeight;
  private Float userHeight;
  private Float currentIMC;
}