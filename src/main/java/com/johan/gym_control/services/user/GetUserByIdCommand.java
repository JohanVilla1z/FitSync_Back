package com.johan.gym_control.services.user;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.Optional;

public class GetUserByIdCommand implements ICommandParametrized<Optional<User>, Long> {
  private final UserRepository userRepository;

  public GetUserByIdCommand(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> execute(Long id) {
    return userRepository.findById(id);
  }
}