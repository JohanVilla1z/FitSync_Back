package com.johan.gym_control.controllers.trainer;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.dto.trainer.TrainerProfileResponse;
import com.johan.gym_control.models.dto.trainer.TrainerProfileUpdateRequest;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.services.trainer.UpdateTrainerProfileCommand;
import com.johan.gym_control.utils.UserMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trainer/profile")
@RequiredArgsConstructor
public class TrainerProfileController {

  private final TrainerRepository trainerRepository;
  private final UserMapper userMapper;

  @Operation(summary = "Actualizar perfil de entrenador")
  @ApiResponse(responseCode = "200", description = "Perfil actualizado correctamente")
  @ApiResponse(responseCode = "400", description = "Datos inválidos")
  @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
  @PutMapping
  public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(
      Authentication authentication,
      @Valid @RequestBody TrainerProfileUpdateRequest request) {

    String email = authentication.getName();

    // Crear directamente el comando y ejecutarlo
    UpdateTrainerProfileCommand command = new UpdateTrainerProfileCommand(trainerRepository, userMapper);
    TrainerProfileResponse response = command.execute(
        new UpdateTrainerProfileCommand.UpdateTrainerProfileParams(email, request));

    return ResponseEntity.ok(response);
  }
}