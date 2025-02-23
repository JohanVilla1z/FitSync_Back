package com.johan.gym_control.services.user;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommand;

public class CreateUserCommand implements ICommand<User> {
  private final UserRepository userRepository;
  private final User user;

  public CreateUserCommand(UserRepository userRepository, User user) {
    this.userRepository = userRepository;
    this.user = user;
  }

  @Override
  public User execute() {
    return userRepository.save(user);
  }
}