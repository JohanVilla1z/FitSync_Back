package com.johan.gym_control.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.johan.gym_control.services.observers.interfaces.IMCObservable;
import com.johan.gym_control.services.observers.interfaces.IMCObserver;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Table(name = "Usuarios")
public class User extends Person implements IMCObservable {

  @Column(name = "userLastName", nullable = false)
  @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
  private String userLastName;

  @Column(name = "is_active", nullable = false)
  @NotNull(message = "El estado de actividad no puede estar vacío")
  private Boolean isActive = true;

  @Temporal(TemporalType.DATE)
  @Column(name = "registerDate", nullable = false)
  @NotNull(message = "La fecha de registro no puede estar vacía")
  private Date registerDate;

  @Column(name = "userPhone")
  @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
  private String userPhone;

  @Column(name = "userWeight")
  @NotNull(message = "El peso no puede estar vacío")
  @Positive(message = "El peso debe ser un valor positivo")
  @DecimalMax(value = "300.0", message = "El peso no puede ser mayor a 300 kg")
  @DecimalMin(value = "20.0", message = "El peso no puede ser menor a 20 kg")
  private Float userWeight;

  @Column(name = "userHeight")
  @NotNull(message = "La altura no puede estar vacía")
  @Positive(message = "La altura debe ser un valor positivo")
  @DecimalMax(value = "2.5", message = "La altura no puede ser mayor a 2.5 metros")
  @DecimalMin(value = "0.5", message = "La altura no puede ser menor a 0.5 metros")
  private Float userHeight;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<IMCTracking> imcTrackings;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EntryLog> entryLogs;

  @ManyToOne
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  private transient List<IMCObserver> observers = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    setRole(Role.USER);
  }

  @Override
  public void addObserver(IMCObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(IMCObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (IMCObserver observer : observers) {
      observer.updateIMC(this);
    }
  }

  public void setUserWeight(Float weight) {
    this.userWeight = weight;
    notifyObservers();
  }

  public void setUserHeight(Float height) {
    this.userHeight = height;
    notifyObservers();
  }
}