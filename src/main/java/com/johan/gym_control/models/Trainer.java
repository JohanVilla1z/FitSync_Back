package com.johan.gym_control.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Entrenadores")
public class Trainer extends Person {
  @OneToMany(mappedBy = "trainer")
  @Size(max = 5, message = "Un entrenador no puede tener más de 5 usuarios asignados")
  private List<User> users = new ArrayList<>();

  @Column(nullable = false)
  private Boolean isActive = true;

  public Boolean isTrainerAvailable() {
    return users.size() < 5 && isActive;
  }
}