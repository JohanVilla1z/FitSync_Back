package com.johan.gym_control.services.user;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class ToggleUserActiveStatusCommand implements ICommandParametrized<User, Long> {
  private final UserRepository userRepository;

  public ToggleUserActiveStatusCommand(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User execute(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    // Toggle the active status
    user.setIsActive(!user.getIsActive());
    return userRepository.save(user);
  }
}