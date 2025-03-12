package com.johan.gym_control.services.user;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.user.UserProfileUpdateRequest;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import com.johan.gym_control.services.observers.IMCTrackingObserver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

public class UpdateUserProfileCommand implements ICommandParametrized<User, UpdateUserProfileCommand.UpdateProfileParams> {
  private final UserRepository userRepository;
  private final IMCTrackingObserver imcTrackingObserver;

  public UpdateUserProfileCommand(UserRepository userRepository, IMCTrackingObserver imcTrackingObserver) {
    this.userRepository = userRepository;
    this.imcTrackingObserver = imcTrackingObserver;
  }

  @Override
  public User execute(UpdateProfileParams params) {
    User user = userRepository.findByEmail(params.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + params.getEmail()));

    // Registrar el observer para IMC
    user.addObserver(imcTrackingObserver);

    // Actualizar campos básicos
    user.setName(params.getRequest().getName());
    user.setUserLastName(params.getRequest().getLastName());
    user.setEmail(params.getRequest().getEmail());
    user.setUserPhone(params.getRequest().getPhone());

    // Detectar cambios en peso o altura
    boolean weightChanged = params.getRequest().getWeight() != null &&
            !Objects.equals(user.getUserWeight(), params.getRequest().getWeight());
    boolean heightChanged = params.getRequest().getHeight() != null &&
            !Objects.equals(user.getUserHeight(), params.getRequest().getHeight());

    // Actualizar peso y altura si hay cambios (esto disparará el observer)
    if (weightChanged) {
      user.setUserWeight(params.getRequest().getWeight());
    }

    if (heightChanged) {
      user.setUserHeight(params.getRequest().getHeight());
    }

    // Notificar cambios para actualizar IMC si es necesario
    if (weightChanged || heightChanged) {
      user.notifyObservers();
    }

    // Guardar y devolver el usuario actualizado
    return userRepository.save(user);
  }

  @AllArgsConstructor
  @Getter
  public static class UpdateProfileParams {
    private String email;
    private UserProfileUpdateRequest request;
  }
}