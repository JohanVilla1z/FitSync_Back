package com.johan.gym_control.services.trainer;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.dto.trainer.TrainerProfileResponse;
import com.johan.gym_control.models.dto.trainer.TrainerProfileUpdateRequest;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import com.johan.gym_control.utils.UserMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UpdateTrainerProfileCommand
    implements ICommandParametrized<TrainerProfileResponse, UpdateTrainerProfileCommand.UpdateTrainerProfileParams> {

  private final TrainerRepository trainerRepository;
  private final UserMapper userMapper;

  public UpdateTrainerProfileCommand(TrainerRepository trainerRepository, UserMapper userMapper) {
    this.trainerRepository = trainerRepository;
    this.userMapper = userMapper;
  }

  @Override
  public TrainerProfileResponse execute(UpdateTrainerProfileParams params) {
    Trainer trainer = trainerRepository.findByEmail(params.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con email: " + params.getEmail()));

    // Actualizar campos
    trainer.setName(params.getRequest().getName());
    trainer.setEmail(params.getRequest().getEmail());

    // Guardar y convertir a DTO para respuesta
    Trainer updatedTrainer = trainerRepository.save(trainer);

    return TrainerProfileResponse.builder()
        .id(updatedTrainer.getId())
        .name(updatedTrainer.getName())
        .email(updatedTrainer.getEmail())
        .isActive(updatedTrainer.getIsActive())
        .isAvailable(updatedTrainer.isTrainerAvailable())
        .users(userMapper.toUserSimpleDTOList(updatedTrainer.getUsers()))
        .build();
  }

  @AllArgsConstructor
  @Getter
  public static class UpdateTrainerProfileParams {
    private String email;
    private TrainerProfileUpdateRequest request;
  }
}