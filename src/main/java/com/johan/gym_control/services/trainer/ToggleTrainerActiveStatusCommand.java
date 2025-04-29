package com.johan.gym_control.services.trainer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ToggleTrainerActiveStatusCommand implements ICommandParametrized<Trainer, Long> {
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private static final Logger logger = LoggerFactory.getLogger(ToggleTrainerActiveStatusCommand.class);

  @Override
  public Trainer execute(Long id) {
    // Recuperar el entrenador con usuarios usando fetch join para evitar problemas
    // de lazy loading
    Trainer trainer = trainerRepository.findByIdWithUsers(id)
        .orElseThrow(() -> new IllegalArgumentException("Trainer with ID " + id + " does not exist"));

    Boolean currentStatus = trainer.getIsActive();
    if (currentStatus == null) {
      logger.warn("Trainer with ID {} has null isActive, defaulting to false", id);
      currentStatus = false;
    }
    boolean newStatus = !currentStatus;
    trainer.setIsActive(newStatus);

    logger.info("Toggling trainer ID {} from {} to {}", id, currentStatus, newStatus);

    // If trainer is being deactivated, remove all assigned users
    if (!newStatus) {
      List<User> users = new ArrayList<>(trainer.getUsers());
      for (User user : users) {
        user.setTrainer(null);
        userRepository.save(user);
      }
      trainer.getUsers().clear();
    }

    try {
      Trainer savedTrainer = trainerRepository.save(trainer);
      logger.info("Trainer ID {} saved with isActive={}", savedTrainer.getId(), savedTrainer.getIsActive());
      return savedTrainer;
    } catch (Exception e) {
      logger.error("Error updating trainer status: {}", e.getMessage(), e);
      throw new RuntimeException("Error al actualizar el estado del entrenador: " + e.getMessage(), e);
    }
  }
}