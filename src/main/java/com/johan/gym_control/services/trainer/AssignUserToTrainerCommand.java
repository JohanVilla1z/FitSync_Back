package com.johan.gym_control.services.trainer;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

@Service
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

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));
    Trainer trainer = trainerRepository.findByIdWithUsers(trainerId)
        .orElseThrow(() -> new IllegalArgumentException("Trainer with ID " + trainerId + " does not exist."));

    if (!trainer.isTrainerAvailable()) {
      throw new IllegalStateException("Trainer with ID " + trainerId + " is not available.");
    }

    user.setTrainer(trainer);
    trainer.getUsers().add(user);
    userRepository.save(user);
    trainerRepository.save(trainer);

    return null;
  }
}