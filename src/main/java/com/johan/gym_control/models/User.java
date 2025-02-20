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
    private String userName;

    @Column(name = "userLastName", nullable = false)
    private String userLastName;

    @Temporal(TemporalType.DATE)
    @Column(name = "registerDate", nullable = false)
    private Date registerDate;

    @Column(name = "userEmail", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @Column(name = "userPhone")
    private String userPhone;

    @Column(name = "userWeight")
    private Float userWeight;

    @Column(name = "userHeight")
    private Float userHeight;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IMCTracking> imcTrackings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntryLog> entryLogs;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
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