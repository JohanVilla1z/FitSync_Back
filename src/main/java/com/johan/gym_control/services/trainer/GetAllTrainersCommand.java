package com.johan.gym_control.services.trainer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommand;

@Service
public class GetAllTrainersCommand implements ICommand<List<Trainer>> {
  private final TrainerRepository trainerRepository;

  public GetAllTrainersCommand(TrainerRepository trainerRepository) {
    this.trainerRepository = trainerRepository;
  }

  @Override
  public List<Trainer> execute() {
    // Usar fetch join para evitar problemas de lazy loading al acceder a users
    return trainerRepository.findAllWithUsers();
  }
}