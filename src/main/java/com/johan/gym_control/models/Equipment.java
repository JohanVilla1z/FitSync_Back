package com.johan.gym_control.models;

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
@Table(name = "Equipos")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eqId;
    
    @Column(name = "eqName", nullable = false)
    private String eqName;
    
    @Column(name = "eqDescription")
    private String eqDescription;
    
    @Column(name = "eqAvailable", nullable = false)
    private Boolean eqAvailable;
}
