package com.johan.gym_control.services.user;

import java.util.List;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class GetAllActiveUsersCommand implements ICommandParametrized<List<User>, Boolean> {
  private final UserRepository userRepository;

  public GetAllActiveUsersCommand(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> execute(Boolean active) {
    return userRepository.findByIsActive(active == null ? true : active);
  }
}