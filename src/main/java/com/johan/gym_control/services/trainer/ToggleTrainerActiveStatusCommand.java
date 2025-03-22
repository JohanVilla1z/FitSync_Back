package com.johan.gym_control.services.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ToggleTrainerActiveStatusCommand implements ICommandParametrized<Trainer, Long> {
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  @Override
  public Trainer execute(Long id) {
    Trainer trainer = trainerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Trainer with ID " + id + " does not exist"));

    boolean newStatus = !trainer.getIsActive();
    trainer.setIsActive(newStatus);

    // If trainer is being deactivated, remove all assigned users
    if (!newStatus) {
      List<User> users = new ArrayList<>(trainer.getUsers());
      for (User user : users) {
        user.setTrainer(null);
        userRepository.save(user);
      }
      trainer.getUsers().clear();
    }

    return trainerRepository.save(trainer);
  }
}