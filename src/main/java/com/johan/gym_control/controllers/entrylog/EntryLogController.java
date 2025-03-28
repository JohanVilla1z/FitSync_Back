package com.johan.gym_control.controllers.entrylog;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.entrylog.EntryLogResponseDTO;
import com.johan.gym_control.models.dto.entrylog.UpdateEntryLogEquipmentRequest;
import com.johan.gym_control.repositories.EntryLogRepository;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.entrylog.CreateEntryLogCommand;
import com.johan.gym_control.services.entrylog.GetUserEntryLogsCommand;
import com.johan.gym_control.services.entrylog.UpdateEntryLogEquipmentCommand;
import com.johan.gym_control.services.entrylog.UpdateEntryLogEquipmentCommand.UpdateEquipmentParams;
import com.johan.gym_control.services.equipment.ReturnEquipmentCommand;
import com.johan.gym_control.utils.EntryLogMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entry-logs")
@RequiredArgsConstructor
@Tag(name = "Entry Logs", description = "API para gestión de registros de entrada al gimnasio")
public class EntryLogController {

  private final EntryLogRepository entryLogRepository;
  private final UserRepository userRepository;
  private final EquipmentRepository equipmentRepository;
  private final EntryLogMapper entryLogMapper;

  @Operation(summary = "Registrar entrada", description = "Registra una nueva entrada al gimnasio para el usuario autenticado.")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Entrada registrada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntryLogResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Usuario no activo", content = @Content(mediaType = "application/json"))})
  @PostMapping
  public ResponseEntity<EntryLogResponseDTO> registerEntry(Authentication authentication) {
    String userEmail = authentication.getName();
    User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    if (!user.getIsActive()) {
      throw new IllegalStateException("El usuario no está activo y no puede registrar entradas.");
    }

    CreateEntryLogCommand command = new CreateEntryLogCommand(entryLogRepository);
    EntryLog entryLog = command.execute(user);

    return new ResponseEntity<>(entryLogMapper.convertToDTO(entryLog), HttpStatus.CREATED);
  }

  @Operation(summary = "Actualizar equipos prestados", description = "Actualiza los equipos prestados en un registro de entrada (solo admin/entrenador).")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Equipos actualizados correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntryLogResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "400", description = "Operación inválida o registro muy antiguo", content = @Content(mediaType = "application/json"))})
  @PutMapping("/{logId}/equipment")
  @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
  public ResponseEntity<EntryLogResponseDTO> updateBorrowedEquipment(@Parameter(description = "ID del registro de entrada") @PathVariable Long logId, @Valid @RequestBody UpdateEntryLogEquipmentRequest request) {

    UpdateEntryLogEquipmentCommand command = new UpdateEntryLogEquipmentCommand(entryLogRepository, equipmentRepository);
    UpdateEquipmentParams params = new UpdateEquipmentParams(logId, request.getEquipmentIds());

    EntryLog updatedEntryLog = command.execute(params);
    return ResponseEntity.ok(entryLogMapper.convertToDTO(updatedEntryLog));
  }

  @Operation(summary = "Marcar equipo como devuelto", description = "Marca un equipo como devuelto y disponible (solo admin/entrenador).")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Equipo marcado como disponible"), @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json"))})
  @PostMapping("/equipment/{equipmentId}/return")
  @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
  public ResponseEntity<Void> returnEquipment(@Parameter(description = "ID del equipo a devolver") @PathVariable Long equipmentId) {

    ReturnEquipmentCommand command = new ReturnEquipmentCommand(equipmentRepository);
    command.execute(equipmentId);

    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Obtener historial de entradas", description = "Obtiene el historial de entradas del usuario autenticado.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Historial recuperado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntryLogResponseDTO.class)))})
  @GetMapping("/user-history")
  public ResponseEntity<List<EntryLogResponseDTO>> getUserEntryLogs(Authentication authentication) {
    String userEmail = authentication.getName();
    User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    GetUserEntryLogsCommand command = new GetUserEntryLogsCommand(entryLogRepository);
    List<EntryLog> entryLogs = command.execute(user);

    List<EntryLogResponseDTO> dtos = entryLogs.stream().map(entryLogMapper::convertToDTO).collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "Obtener todos los registros de entrada", description = "Obtiene todos los registros de entrada (solo admin).")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Registros recuperados correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntryLogResponseDTO.class)))})
  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<EntryLogResponseDTO>> getAllEntryLogs() {
    List<EntryLog> entryLogs = entryLogRepository.findAllByOrderByTimestampDesc();

    List<EntryLogResponseDTO> dtos = entryLogs.stream().map(entryLogMapper::convertToDTO).collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
  }
}