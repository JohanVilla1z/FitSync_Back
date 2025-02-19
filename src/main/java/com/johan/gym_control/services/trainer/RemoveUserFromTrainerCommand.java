package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.Optional;

public class RemoveUserFromTrainerCommand implements ICommandParametrized<Void, Long> {
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public RemoveUserFromTrainerCommand(TrainerRepository trainerRepository, UserRepository userRepository) {
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Void execute(Long userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      Trainer trainer = user.getTrainer();
      if (trainer != null) {
        trainer.getUsers().remove(user);
        user.setTrainer(null);
        userRepository.save(user);
        trainerRepository.save(trainer);
      }
    }
    return null;
  }
}