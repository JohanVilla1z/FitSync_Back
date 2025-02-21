package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommand;

import java.util.List;

public class GetAllTrainersCommand implements ICommand<List<Trainer>> {
  private final TrainerRepository trainerRepository;

  public GetAllTrainersCommand(TrainerRepository trainerRepository) {
    this.trainerRepository = trainerRepository;
  }

  @Override
  public List<Trainer> execute() {
    return trainerRepository.findAll();
  }
}