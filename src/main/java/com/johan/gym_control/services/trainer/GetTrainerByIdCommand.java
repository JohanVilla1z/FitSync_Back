package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetTrainerByIdCommand implements ICommandParametrized<Optional<Trainer>, Long> {
  private final TrainerRepository trainerRepository;

  public GetTrainerByIdCommand(TrainerRepository trainerRepository) {
    this.trainerRepository = trainerRepository;
  }

  @Override
  public Optional<Trainer> execute(Long id) {
    return trainerRepository.findById(id);
  }
}
