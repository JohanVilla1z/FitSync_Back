package com.johan.gym_control.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String userName;
    
    @Column(name = "userEmail", nullable = false, unique = true)
    private String userEmail;
    
    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    public Boolean isTrainerAvailable() {
        return users.size() < 5;
    }
}