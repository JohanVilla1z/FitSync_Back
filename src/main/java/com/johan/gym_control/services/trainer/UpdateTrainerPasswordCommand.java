package com.johan.gym_control.services.trainer;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.dto.auth.UpdatePasswordRequest;
import com.johan.gym_control.repositories.TrainerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateTrainerPasswordCommand {
  private final TrainerRepository trainerRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void execute(String email, UpdatePasswordRequest request) {
    // Validar que las contraseñas nuevas coincidan
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("Las contraseñas no coinciden");
    }

    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con email: " + email));

    // Verificar contraseña actual
    if (!passwordEncoder.matches(request.getCurrentPassword(), trainer.getPassword())) {
      throw new AccessDeniedException("La contraseña actual es incorrecta");
    }

    // Actualizar contraseña
    trainer.setPassword(passwordEncoder.encode(request.getNewPassword()));
    trainerRepository.save(trainer);
  }
}