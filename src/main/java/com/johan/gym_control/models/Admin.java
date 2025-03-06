package com.johan.gym_control.models;

import com.johan.gym_control.models.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// @AllArgsConstructor
@Entity
// @Builder
@Table(name = "Administradores")
public class Admin extends Person {

  public Admin(String name, String lastName, String email, String password) {
    super(name, email, password, Role.ADMIN);
  }

  @PrePersist
  public void prePersist() {
    setRole(Role.ADMIN);
  }
}