package com.johan.gym_control.services.user;

import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommand;
import com.johan.gym_control.services.observers.IMCTrackingObserver;

public class CreateUserCommand implements ICommand<User> {
  private final UserRepository userRepository;
  private final User user;
  private final IMCTrackingObserver imcTrackingObserver;

  public CreateUserCommand(UserRepository userRepository, User user,
      IMCTrackingObserver imcTrackingObserver) {
    this.userRepository = userRepository;
    this.user = user;
    this.imcTrackingObserver = imcTrackingObserver;
  }

  @Override
  public User execute() {
    // Primero guardar el usuario para obtener el ID
    User savedUser = userRepository.save(user);

    // Calcular IMC usando el patrón observador
    if (savedUser.getUserWeight() != null && savedUser.getUserHeight() != null &&
        savedUser.getUserWeight() > 0 && savedUser.getUserHeight() > 0) {
      savedUser.addObserver(imcTrackingObserver);
      savedUser.notifyObservers();
    }

    return savedUser;
  }
}