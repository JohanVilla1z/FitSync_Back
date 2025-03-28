package com.johan.gym_control.services.user;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import org.springframework.transaction.annotation.Transactional;

public class ToggleUserActiveStatusCommand implements ICommandParametrized<User, Long> {
  private final UserRepository userRepository;

  public ToggleUserActiveStatusCommand(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  @Override
  public User execute(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    boolean nuevoEstado = !user.getIsActive();
    user.setIsActive(nuevoEstado);

    // Si el usuario se está desactivando y tiene un entrenador asignado
    if (!nuevoEstado && user.getTrainer() != null) {
      Trainer trainer = user.getTrainer();
      trainer.getUsers().remove(user);
      user.setTrainer(null);
      // No necesitas guardar el trainer explícitamente si usas @Transactional
    }

    return userRepository.save(user);
  }
}