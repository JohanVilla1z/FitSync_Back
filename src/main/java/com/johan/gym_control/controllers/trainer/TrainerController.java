package com.johan.gym_control.controllers.trainer;

import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.dto.trainer.TrainerCreateRequest;
import com.johan.gym_control.services.trainer.CreateTrainerCommand;
import com.johan.gym_control.services.trainer.DeleteTrainerCommand;
import com.johan.gym_control.services.trainer.GetAllTrainersCommand;
import com.johan.gym_control.services.trainer.GetTrainerByIdCommand;
import com.johan.gym_control.services.trainer.ToggleTrainerActiveStatusCommand;
import com.johan.gym_control.services.trainer.UpdateTrainerCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

  @PutMapping("/{id}")
  public ResponseEntity<Trainer> updateTrainer(@PathVariable Long id, @RequestBody Trainer trainer) {
    trainer.setId(id); // Asegurarse de que el ID esté configurado
    Trainer updatedTrainer = updateTrainerCommand.execute(trainer);
    return ResponseEntity.ok(updatedTrainer);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
    deleteTrainerCommand.execute(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Trainer> getTrainerById(@PathVariable Long id) {
    return getTrainerByIdCommand.execute(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Trainer>> getAllTrainers() {
    List<Trainer> trainers = getAllTrainersCommand.execute();
    return ResponseEntity.ok(trainers);
  }

  @PutMapping("/{id}/toggle-status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Trainer> toggleTrainerStatus(@PathVariable Long id) {
    Trainer updatedTrainer = toggleTrainerActiveStatusCommand.execute(id);
    return ResponseEntity.ok(updatedTrainer);
  }
}