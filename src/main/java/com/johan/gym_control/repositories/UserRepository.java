package com.johan.gym_control.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByTrainer(Trainer trainer);

    List<User> findByIsActive(Boolean isActive);
}
