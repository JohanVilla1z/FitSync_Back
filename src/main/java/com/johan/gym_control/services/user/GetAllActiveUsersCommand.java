package com.johan.gym_control.services.user;

import java.util.List;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommand;

public class GetAllActiveUsersCommand implements ICommand<List<User>> {
  private final UserRepository userRepository;

  public GetAllActiveUsersCommand(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> execute() {
    return userRepository.findByIsActive(true);
  }
}