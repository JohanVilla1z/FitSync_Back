package com.johan.GymControl.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String adminName;
    
    @Column(name = "adminEmail", nullable = false, unique = true)
    private String adminEmail;
    
    @Column(name = "adminPassword", nullable = false)
    private String adminPassword;
}