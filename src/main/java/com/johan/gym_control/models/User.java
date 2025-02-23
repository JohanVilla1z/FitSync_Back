package com.johan.gym_control.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.johan.gym_control.services.observers.interfaces.IMCObservable;
import com.johan.gym_control.services.observers.interfaces.IMCObserver;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
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
@Table(name = "Usuarios")
public class User implements IMCObservable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "userName", nullable = false)
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String userName;

    @Column(name = "userLastName", nullable = false)
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String userLastName;

    @Column(name = "is_active", nullable = false)
    @NotNull(message = "El estado de actividad no puede estar vacío")
    private Boolean isActive = true;

    @Temporal(TemporalType.DATE)
    @Column(name = "registerDate", nullable = false)
    @NotNull(message = "La fecha de registro no puede estar vacía")
    private Date registerDate;

    @Column(name = "userEmail", nullable = false, unique = true)
    @Email(message = "Debe proporcionar un correo válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String userEmail;

    @Column(name = "userPassword", nullable = false)
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String userPassword;

    @Column(name = "userPhone")
    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String userPhone;

    @Column(name = "userWeight")
    @NotNull(message = "El peso no puede estar vacío")
    private Float userWeight;

    @Column(name = "userHeight")
    @NotNull(message = "La altura no puede estar vacía")
    private Float userHeight;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IMCTracking> imcTrackings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntryLog> entryLogs;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private transient List<IMCObserver> observers = new ArrayList<>();

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