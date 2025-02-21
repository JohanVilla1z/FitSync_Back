package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class UpdateTrainerCommand implements ICommandParametrized<Trainer, Trainer> {
  private final TrainerRepository trainerRepository;

  public UpdateTrainerCommand(TrainerRepository trainerRepository) {
    this.trainerRepository = trainerRepository;
  }

  @Override
  public Trainer execute(Trainer trainer) {
    return trainerRepository.save(trainer);
  }
}