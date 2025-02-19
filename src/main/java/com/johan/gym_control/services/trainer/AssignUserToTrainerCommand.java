package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.Optional;

public class AssignUserToTrainerCommand implements ICommandParametrized<Void, Long[]> {
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public AssignUserToTrainerCommand(TrainerRepository trainerRepository, UserRepository userRepository) {
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Void execute(Long[] params) {
    Long userId = params[0];
    Long trainerId = params[1];
    Optional<User> userOpt = userRepository.findById(userId);
    Optional<Trainer> trainerOpt = trainerRepository.findById(trainerId);

    if (userOpt.isPresent() && trainerOpt.isPresent()) {
      User user = userOpt.get();
      Trainer trainer = trainerOpt.get();

      if (trainer.isTrainerAvailable()) {
        user.setTrainer(trainer);
        trainer.getUsers().add(user);
        userRepository.save(user);
        trainerRepository.save(trainer);
      }
    }
    return null;
  }
}