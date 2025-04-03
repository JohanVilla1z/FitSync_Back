package com.johan.gym_control.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.models.dto.user.UserSimpleDTO;

@Component
public class UserMapper {

  /**
   * Convierte una entidad User a UserProfileResponse (para respuestas detalladas
   * de perfil)
   */
  public UserProfileResponse convertToDTO(User user) {
    if (user == null) {
      return null;
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
        .currentIMC(user.getCurrentIMC())
        .trainerName(user.getTrainer() != null ? user.getTrainer().getName() : null)
        .trainerEmail(user.getTrainer() != null ? user.getTrainer().getEmail() : null)
        .build();
  }

  /**
   * Convierte una entidad User a UserSimpleDTO (versión simplificada sin
   * referencias circulares)
   * Usado principalmente para evitar referencias circulares en relaciones
   * bidireccionales
   */
  public UserSimpleDTO toUserSimpleDTO(User user) {
    if (user == null) {
      return null;
    }

    return UserSimpleDTO.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .userLastName(user.getUserLastName())
        .isActive(user.getIsActive())
        .registerDate(user.getRegisterDate())
        .userPhone(user.getUserPhone())
        .userWeight(user.getUserWeight())
        .userHeight(user.getUserHeight())
        .currentIMC(user.getCurrentIMC())
        // No incluimos trainer para evitar la referencia circular
        .build();
  }

  /**
   * Convierte una lista de entidades User a una lista de UserSimpleDTO
   * Utilizado para colecciones de usuarios en relaciones como Trainer->Users
   */
  public List<UserSimpleDTO> toUserSimpleDTOList(List<User> users) {
    if (users == null) {
      return List.of(); // Devuelve lista vacía en lugar de null
    }

    return users.stream()
        .map(this::toUserSimpleDTO)
        .collect(Collectors.toList());
  }

  /**
   * Convierte una lista de entidades User a una lista de UserProfileResponse
   */
  public List<UserProfileResponse> toUserProfileResponseList(List<User> users) {
    if (users == null) {
      return List.of();
    }

    return users.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }
}