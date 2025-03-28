package com.johan.gym_control.utils;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.trainer.TrainerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TrainerMapper {

  public TrainerResponseDTO convertToDTO(Trainer trainer) {
    return TrainerResponseDTO.builder()
            .id(trainer.getId())
            .name(trainer.getName())
            .email(trainer.getEmail())
            .isActive(trainer.getIsActive())
            .isAvailable(trainer.isTrainerAvailable())  // Agregamos esta línea
            .userIds(trainer.getUsers().stream().map(User::getId).collect(Collectors.toList()))
            .build();
  }
}