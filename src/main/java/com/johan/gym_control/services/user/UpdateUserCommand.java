package com.johan.gym_control.services.user;

import java.util.Optional;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.imcTracking.UpdateIMCCommand;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import com.johan.gym_control.services.observers.IMCTrackingObserver;

public class UpdateUserCommand implements ICommandParametrized<Void, User> {
  private final UserRepository userRepository;
  private final IMCTrackingObserver imcTrackingObserver;

  public UpdateUserCommand(UserRepository userRepository, UpdateIMCCommand updateIMCCommand,
      IMCTrackingObserver imcTrackingObserver) {
    this.userRepository = userRepository;
    this.imcTrackingObserver = imcTrackingObserver;
  }

  @Override
  public Void execute(User user) {
    Optional<User> userOpt = userRepository.findById(user.getUserId());
    if (userOpt.isPresent()) {
      User existingUser = userOpt.get();
      existingUser.addObserver(imcTrackingObserver);
      existingUser.setUserName(user.getUserName());
      existingUser.setUserLastName(user.getUserLastName());
      existingUser.setUserPhone(user.getUserPhone());
      existingUser.setUserWeight(user.getUserWeight());
      existingUser.setUserHeight(user.getUserHeight());
      userRepository.save(existingUser);
    }
    return null;
  }
}