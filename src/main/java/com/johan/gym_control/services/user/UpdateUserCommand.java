package com.johan.gym_control.services.user;

import java.util.Objects;
import java.util.Optional;

import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.user.UserUpdateRequest;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import com.johan.gym_control.services.observers.IMCTrackingObserver;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserCommand implements ICommandParametrized<User, UserUpdateRequest> {
  private final UserRepository userRepository;
  private final IMCTrackingObserver imcTrackingObserver;

  public UpdateUserCommand(UserRepository userRepository, IMCTrackingObserver imcTrackingObserver) {
    this.userRepository = userRepository;
    this.imcTrackingObserver = imcTrackingObserver;
  }

  @Override
  public User execute(UserUpdateRequest user) {
    Optional<User> userOpt = userRepository.findById(user.getId());
    if (userOpt.isPresent()) {
      User existingUser = userOpt.get();

      existingUser.addObserver(imcTrackingObserver);

      existingUser.setName(user.getName());
      existingUser.setUserLastName(user.getLastName());
      existingUser.setEmail(user.getEmail());
      existingUser.setUserPhone(user.getPhone());
      existingUser.setIsActive(user.getIsActive());

      boolean weightChanged = !Objects.equals(existingUser.getUserWeight(), user.getWeight());
      boolean heightChanged = !Objects.equals(existingUser.getUserHeight(), user.getHeight());

      if (weightChanged || heightChanged) {
        existingUser.setUserWeight(user.getWeight());
        existingUser.setUserHeight(user.getHeight());
        existingUser.notifyObservers();
      }

      return userRepository.save(existingUser);
    }
    throw new IllegalArgumentException("User not found with id: " + user.getId());
  }
}