package com.johan.gym_control.models;

import java.util.HashSet;
import java.util.Set;

import com.johan.gym_control.models.enums.EquipmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
  @Size(min = 2, max = 100, message = "El nombre del equipo debe tener entre 2 y 100 caracteres")
  @NotBlank(message = "El nombre del equipo no puede estar vacío")
  private String eqName;

  @Column(name = "eqDescription")
  @Size(max = 255, message = "La descripción del equipo no puede tener más de 255 caracteres")
  private String eqDescription;

  @Column(name = "eqStatus", nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull(message = "El estado del equipo no puede estar vacío")
  private EquipmentStatus eqStatus;

  // Mantener el campo antiguo por compatibilidad con la base de datos
  @Column(name = "eqAvailable", nullable = false)
  private Boolean eqAvailable = true;

  @Column(name = "eqLoanCount", nullable = false)
  private Integer eqLoanCount = 0;

  @ManyToMany(mappedBy = "borrowedEquipment")
  private Set<EntryLog> entryLogs = new HashSet<>();

  // Sincronizar automáticamente eqAvailable con eqStatus
  @PrePersist
  @PreUpdate
  private void syncAvailableWithStatus() {
    // Si el estado es AVAILABLE, entonces eqAvailable = true
    this.eqAvailable = (eqStatus == EquipmentStatus.AVAILABLE);
  }
}