package com.johan.gym_control.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
// @NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Administradores")
public class Admin extends Person {

  @PrePersist
  public void prePersist() {
    setRole(Role.ADMIN);
  }
}