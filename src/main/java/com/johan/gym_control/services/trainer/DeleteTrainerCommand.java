package com.johan.gym_control.services.trainer;

import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import org.springframework.stereotype.Service;

@Service
public class DeleteTrainerCommand implements ICommandParametrized<Void, Long> {
  private final TrainerRepository trainerRepository;

  public DeleteTrainerCommand(TrainerRepository trainerRepository) {
    this.trainerRepository = trainerRepository;
  }

  @Override
  public Void execute(Long id) {
    if (!trainerRepository.existsById(id)) {
      throw new IllegalArgumentException("Trainer with ID " + id + " does not exist.");
    }
    trainerRepository.deleteById(id);
    return null;
  }
}