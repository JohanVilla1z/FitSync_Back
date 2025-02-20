package com.johan.gym_control.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Administradores")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(name = "adminName", nullable = false)
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String adminName;

    @Column(name = "adminEmail", nullable = false, unique = true)
    @Email(message = "Debe proporcionar un correo válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String adminEmail;

    @Column(name = "adminPassword", nullable = false)
    private String adminPassword;
}