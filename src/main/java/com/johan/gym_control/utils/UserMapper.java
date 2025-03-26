package com.johan.gym_control.utils;

import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserProfileResponse convertToDTO(User user) {
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
            .currentIMC(user.getCurrentIMC())
            .trainerName(user.getTrainer() != null ? user.getTrainer().getName() : null) // Incluir el nombre del entrenador
            .build();
  }
}