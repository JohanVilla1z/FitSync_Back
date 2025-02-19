package com.johan.gym_control.repositories;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByTrainer(Trainer trainer);
}
