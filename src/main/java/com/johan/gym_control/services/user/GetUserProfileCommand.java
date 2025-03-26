package com.johan.gym_control.services.user;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.IMCTracking;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.repositories.IMCTrackingRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.List;

public class GetUserProfileCommand implements ICommandParametrized<UserProfileResponse, String> {
  private final UserRepository userRepository;
  private final IMCTrackingRepository imcTrackingRepository;

  public GetUserProfileCommand(UserRepository userRepository, IMCTrackingRepository imcTrackingRepository) {
    this.userRepository = userRepository;
    this.imcTrackingRepository = imcTrackingRepository;
  }

  @Override
  public UserProfileResponse execute(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

    // Obtener el IMC más reciente
    Float currentIMC = null;
    List<IMCTracking> imcTrackings = imcTrackingRepository.findByUserOrderByMeasurementDateDesc(user);
    if (imcTrackings != null && !imcTrackings.isEmpty()) {
      currentIMC = imcTrackings.get(0).getImcValue();
    }

    // Obtener nombre del entrenador si existe
    String trainerName = null;
    String trainerEmail = null;
    if (user.getTrainer() != null) {
      trainerName = user.getTrainer().getName();
      trainerEmail = user.getTrainer().getEmail();
    }

    return UserProfileResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .lastName(user.getUserLastName())
            .email(user.getEmail())
            .phone(user.getUserPhone())
            .weight(user.getUserWeight())
            .height(user.getUserHeight())
            .isActive(user.getIsActive())
            .registerDate(user.getRegisterDate())
            .currentIMC(currentIMC)
            .trainerName(trainerName)
            .trainerEmail(trainerEmail)
            .build();
  }
}