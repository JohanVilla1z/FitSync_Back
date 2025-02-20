package com.johan.gym_control.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "Entrenadores")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "userName", nullable = false)
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String userName;

    @Column(name = "userEmail", nullable = false, unique = true)
    @Email(message = "Debe proporcionar un correo válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String userEmail;

    @Column(name = "userPassword", nullable = false)
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String userPassword;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    public Boolean isTrainerAvailable() {
        return users.size() < 5;
    }
}