package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommand;

public class CreateTrainerCommand implements ICommand<Trainer> {
  private final TrainerRepository trainerRepository;
  private final Trainer trainer;

  public CreateTrainerCommand(TrainerRepository trainerRepository, Trainer trainer) {
    this.trainerRepository = trainerRepository;
    this.trainer = trainer;
  }

  @Override
  public Trainer execute() {
    return trainerRepository.save(trainer);
  }
}
