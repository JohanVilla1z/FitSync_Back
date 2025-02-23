package com.johan.gym_control.services.user;

import java.util.Objects;
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

      // Restaurar observadores solo si es necesario
      if (existingUser.getObservers() == null || !existingUser.getObservers().contains(imcTrackingObserver)) {
        existingUser.addObserver(imcTrackingObserver);
      }

      // Actualizar datos básicos
      existingUser.setUserName(user.getUserName());
      existingUser.setUserLastName(user.getUserLastName());
      existingUser.setUserPhone(user.getUserPhone());

      // Verificar si hubo cambios en peso o altura
      boolean weightChanged = !Objects.equals(existingUser.getUserWeight(), user.getUserWeight());
      boolean heightChanged = !Objects.equals(existingUser.getUserHeight(), user.getUserHeight());

      // Actualizar peso y altura solo si han cambiado
      if (weightChanged) {
        existingUser.setUserWeight(user.getUserWeight());
      }
      if (heightChanged) {
        existingUser.setUserHeight(user.getUserHeight());
      }

      userRepository.save(existingUser);
    }
    return null;
  }
}