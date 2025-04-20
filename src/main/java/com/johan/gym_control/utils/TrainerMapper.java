package com.johan.gym_control.utils;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.trainer.TrainerResponseDTO;

@Component
public class TrainerMapper {

  public TrainerResponseDTO convertToDTO(Trainer trainer) {
    return TrainerResponseDTO.builder()
        .id(trainer.getId())
        .name(trainer.getName())
        .email(trainer.getEmail())
        .active(trainer.getIsActive())
        .available(trainer.isTrainerAvailable())
        .userIds(trainer.getUsers().stream().map(User::getId).collect(Collectors.toList()))
        .build();
  }
}