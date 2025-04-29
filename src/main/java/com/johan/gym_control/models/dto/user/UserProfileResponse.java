package com.johan.gym_control.models.dto.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private Float weight;
    private Float height;
    private Boolean isActive;
    private Date registerDate;
    private Float currentIMC;
    private String trainerName; // Nombre del entrenador asignado (si existe)
    private String trainerEmail; // Email del entrenador asignado (si existe)
}