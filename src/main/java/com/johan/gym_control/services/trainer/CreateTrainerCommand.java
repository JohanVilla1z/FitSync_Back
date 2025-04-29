package com.johan.gym_control.services.trainer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.dto.trainer.TrainerCreateRequest;
import com.johan.gym_control.models.enums.Role;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

@Service
public class CreateTrainerCommand implements ICommandParametrized<Trainer, TrainerCreateRequest> {
  private final TrainerRepository trainerRepository;
  private final PasswordEncoder passwordEncoder;

  public CreateTrainerCommand(TrainerRepository trainerRepository, PasswordEncoder passwordEncoder) {
    this.trainerRepository = trainerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Trainer execute(TrainerCreateRequest request) {
    // Verificar si ya existe un entrenador con ese email
    if (trainerRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException("Ya existe un entrenador con ese email");
    }

    Trainer trainer = new Trainer();
    trainer.setName(request.getName());
    trainer.setEmail(request.getEmail());
    trainer.setPassword(passwordEncoder.encode(request.getPassword()));
    // Si el request trae null, asigna true por defecto
    trainer.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    trainer.setRole(Role.TRAINER);

    return trainerRepository.save(trainer);
  }
}