package com.johan.gym_control.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.johan.gym_control.models.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByEmail(String email);

    /**
     * Obtiene todos los entrenadores junto con sus usuarios asignados usando fetch
     * join
     * para evitar problemas de lazy loading (N+1 problem) al acceder a la colección
     * users.
     */
    @Query("SELECT DISTINCT t FROM Trainer t LEFT JOIN FETCH t.users")
    List<Trainer> findAllWithUsers();

    /**
     * Obtiene un entrenador por ID junto con sus usuarios asignados usando fetch
     * join
     * para evitar problemas de lazy loading al acceder a la colección users.
     */
    @Query("SELECT t FROM Trainer t LEFT JOIN FETCH t.users WHERE t.id = :id")
    Optional<Trainer> findByIdWithUsers(Long id);
}
