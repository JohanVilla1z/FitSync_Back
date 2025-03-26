package com.johan.gym_control.utils;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.trainer.TrainerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TrainerMapper {

  public TrainerResponseDTO convertToDTO(Trainer trainer) {
    Set<User> usersSet = new HashSet<>(trainer.getUsers());
    return TrainerResponseDTO.builder()
            .id(trainer.getId())
            .name(trainer.getName())
            .email(trainer.getEmail())
            .isActive(trainer.getIsActive())
            .userIds(usersSet.stream().map(User::getId).toList())
            .build();
  }
}