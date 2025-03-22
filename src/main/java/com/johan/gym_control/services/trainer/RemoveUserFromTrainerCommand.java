package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class RemoveUserFromTrainerCommand implements ICommandParametrized<Void, Long> {
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public RemoveUserFromTrainerCommand(TrainerRepository trainerRepository, UserRepository userRepository) {
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Void execute(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

    Trainer trainer = user.getTrainer();
    if (trainer == null) {
      throw new IllegalStateException("User with ID " + userId + " is not assigned to any trainer.");
    }

    trainer.getUsers().remove(user);
    user.setTrainer(null);
    userRepository.save(user);
    trainerRepository.save(trainer);

    return null;
  }
}