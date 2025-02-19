package com.johan.gym_control.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuarios")
public class User {
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
}