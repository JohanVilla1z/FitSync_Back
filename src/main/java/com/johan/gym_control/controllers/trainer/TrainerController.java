package com.johan.gym_control.controllers.trainer;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.auth.UpdatePasswordRequest;
import com.johan.gym_control.models.dto.trainer.AssignUserRequest;
import com.johan.gym_control.models.dto.trainer.TrainerCreateRequest;
import com.johan.gym_control.models.dto.trainer.TrainerResponseDTO;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.services.trainer.AssignUserToTrainerCommand;
import com.johan.gym_control.services.trainer.CreateTrainerCommand;
import com.johan.gym_control.services.trainer.DeleteTrainerCommand;
import com.johan.gym_control.services.trainer.GetAllTrainersCommand;
import com.johan.gym_control.services.trainer.GetTrainerByIdCommand;
import com.johan.gym_control.services.trainer.ToggleTrainerActiveStatusCommand;
import com.johan.gym_control.services.trainer.UpdateTrainerCommand;
import com.johan.gym_control.services.trainer.UpdateTrainerPasswordCommand;
import com.johan.gym_control.utils.TrainerMapper;
import com.johan.gym_control.utils.UserMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {
  private final CreateTrainerCommand createTrainerCommand;
  private final UpdateTrainerCommand updateTrainerCommand;
  private final DeleteTrainerCommand deleteTrainerCommand;
  private final GetTrainerByIdCommand getTrainerByIdCommand;
  private final GetAllTrainersCommand getAllTrainersCommand;
  private final ToggleTrainerActiveStatusCommand toggleTrainerActiveStatusCommand;
  private final AssignUserToTrainerCommand assignUserToTrainerCommand;
  private final TrainerMapper trainerMapper;
  private final UserMapper userMapper;
  private final UpdateTrainerPasswordCommand updateTrainerPasswordCommand;

  @Operation(summary = "Crear un nuevo entrenador", description = "Permite crear un nuevo entrenador en el sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Entrenador creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Trainer.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json"))
  })
  @PostMapping
  public ResponseEntity<Trainer> createTrainer(@Valid @RequestBody TrainerCreateRequest request) {
    Trainer createdTrainer = createTrainerCommand.execute(request);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdTrainer.getId())
        .toUri();
    return ResponseEntity.created(location).body(createdTrainer);
  }

  @Operation(summary = "Actualizar un entrenador", description = "Permite actualizar los datos de un entrenador existente.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Entrenador actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Trainer.class))),
      @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/{id}")
  public ResponseEntity<Trainer> updateTrainer(@PathVariable Long id, @RequestBody Trainer trainer) {
    trainer.setId(id); // Asegurarse de que el ID esté configurado
    Trainer updatedTrainer = updateTrainerCommand.execute(trainer);
    return ResponseEntity.ok(updatedTrainer);
  }

  @Operation(summary = "Eliminar un entrenador", description = "Permite eliminar un entrenador del sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Entrenador eliminado exitosamente"),
      @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json"))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
    deleteTrainerCommand.execute(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Obtener un entrenador por su ID", description = "Devuelve los detalles de un entrenador específico.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Entrenador encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Trainer.class))),
      @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/{id}")
  public ResponseEntity<TrainerResponseDTO> getTrainerById(@PathVariable Long id) {
    return getTrainerByIdCommand.execute(id)
        .map(trainer -> {
          TrainerResponseDTO dto = trainerMapper.convertToDTO(trainer);
          dto.setAvailable(trainer.isTrainerAvailable());
          return ResponseEntity.ok(dto);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<TrainerResponseDTO>> getAllTrainers() {
    List<Trainer> trainers = getAllTrainersCommand.execute();
    List<TrainerResponseDTO> dtos = trainers.stream()
        .map(trainer -> {
          TrainerResponseDTO dto = trainerMapper.convertToDTO(trainer);
          dto.setAvailable(trainer.isTrainerAvailable());
          return dto;
        })
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "Cambiar el estado activo de un entrenador", description = "Permite alternar el estado activo de un entrenador.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estado del entrenador cambiado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Trainer.class))),
      @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/{id}/toggle-status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Trainer> toggleTrainerStatus(@PathVariable Long id) {
    Trainer updatedTrainer = toggleTrainerActiveStatusCommand.execute(id);
    return ResponseEntity.ok(updatedTrainer);
  }

  @Operation(summary = "Asignar un usuario a un entrenador", description = "Asigna un usuario a la lista de usuarios de un entrenador.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario asignado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Usuario o entrenador no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/assign-user")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> assignUserToTrainer(@Valid @RequestBody AssignUserRequest request) {
    Long[] params = { request.getUserId(), request.getTrainerId() };
    assignUserToTrainerCommand.execute(params);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Actualizar contraseña del entrenador autenticado", description = "Permite al entrenador actualizar su contraseña.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "403", description = "Contraseña actual incorrecta", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/update-password")
  public ResponseEntity<Void> updateTrainerPassword(
      @Valid @RequestBody UpdatePasswordRequest request,
      Authentication authentication) {
    String trainerEmail = authentication.getName();
    updateTrainerPasswordCommand.execute(trainerEmail, request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Obtener usuarios asignados a un entrenador", description = "Devuelve todos los usuarios asignados a un entrenador específico.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "404", description = "Entrenador no encontrado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/{trainerId}/users")
  public ResponseEntity<List<UserProfileResponse>> getTrainerUsers(@PathVariable Long trainerId) {
    // Verificar que el entrenador existe
    Optional<Trainer> trainerOpt = getTrainerByIdCommand.execute(trainerId);
    if (trainerOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    // Obtener los usuarios asignados al entrenador
    Trainer trainer = trainerOpt.get();
    List<User> users = trainer.getUsers();

    // Convertir a DTOs
    List<UserProfileResponse> userResponses = users.stream()
        .map(userMapper::convertToDTO)
        .collect(Collectors.toList());

    return ResponseEntity.ok(userResponses);
  }
}