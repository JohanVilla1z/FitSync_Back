package com.johan.gym_control.controllers.equipment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentRequestDTO;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.CreateEquipmentCommand;
import com.johan.gym_control.services.equipment.DeleteEquipmentCommand;
import com.johan.gym_control.services.equipment.GetAllEquipmentCommand;
import com.johan.gym_control.services.equipment.GetEquipmentByIdCommand;
import com.johan.gym_control.services.equipment.ToggleEquipmentAvailabilityCommand;
import com.johan.gym_control.services.equipment.UpdateEquipmentCommand;
import com.johan.gym_control.utils.EquipmentMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {
  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;
  private final ToggleEquipmentAvailabilityCommand toggleCommand;

  @Operation(summary = "Crear un nuevo equipo", description = "Permite crear un nuevo equipo en el sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json"))
  })
  @PostMapping
  public ResponseEntity<EquipmentResponseDTO> createEquipment(
      @Valid @RequestBody EquipmentRequestDTO equipmentRequestDTO) {
    Equipment equipment = equipmentMapper.toEntity(equipmentRequestDTO);
    CreateEquipmentCommand createCommand = new CreateEquipmentCommand(equipmentRepository, equipment);
    Equipment savedEquipment = createCommand.execute();
    return new ResponseEntity<>(equipmentMapper.toDto(savedEquipment), HttpStatus.CREATED);
  }

  @Operation(summary = "Obtener todos los equipos", description = "Devuelve una lista de todos los equipos registrados.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de equipos obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentResponseDTO.class)))
  })
  @GetMapping
  public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
    try {
      GetAllEquipmentCommand getAllCommand = new GetAllEquipmentCommand(equipmentRepository);
      List<Equipment> equipments = getAllCommand.execute();
      List<EquipmentResponseDTO> equipmentDTOs = equipments.stream()
          .map(equipmentMapper::toDto)
          .collect(Collectors.toList());
      return ResponseEntity.ok(equipmentDTOs);
    } catch (Exception e) {
      // Log the exception if you have a logger, e.g., log.error("Error fetching
      // equipment", e);
      String errorJson = "{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}";
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .header("Content-Type", "application/json")
          .body(null); // Optionally, you can return a ResponseEntity with a body containing errorJson
    }
  }

  @Operation(summary = "Obtener un equipo por su ID", description = "Devuelve los detalles de un equipo específico.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Equipo encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/{id}")
  public ResponseEntity<EquipmentResponseDTO> getEquipmentById(@PathVariable Long id) {
    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);
    Optional<Equipment> equipmentOptional = getByIdCommand.execute(id);

    return equipmentOptional
        .map(equipment -> ResponseEntity.ok(equipmentMapper.toDto(equipment)))
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Actualizar un equipo existente", description = "Permite actualizar los detalles de un equipo.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Equipo actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/{id}")
  public ResponseEntity<EquipmentResponseDTO> updateEquipment(
      @PathVariable Long id,
      @Valid @RequestBody EquipmentRequestDTO equipmentRequestDTO) {

    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);
    Optional<Equipment> equipmentOptional = getByIdCommand.execute(id);

    if (equipmentOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Equipment equipment = equipmentOptional.get();
    equipmentMapper.updateEntityFromDto(equipment, equipmentRequestDTO);

    UpdateEquipmentCommand updateCommand = new UpdateEquipmentCommand(equipmentRepository);
    Equipment updatedEquipment = updateCommand.execute(equipment);

    return ResponseEntity.ok(equipmentMapper.toDto(updatedEquipment));
  }

  @Operation(summary = "Eliminar un equipo", description = "Permite eliminar un equipo del sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Equipo eliminado exitosamente"),
      @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json"))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);

    if (getByIdCommand.execute(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    DeleteEquipmentCommand deleteCommand = new DeleteEquipmentCommand(equipmentRepository);
    deleteCommand.execute(id);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Cambiar la disponibilidad de un equipo", description = "Permite alternar la disponibilidad de un equipo.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Disponibilidad del equipo cambiada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Equipo no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/{id}/toggle-availability")
  public ResponseEntity<EquipmentResponseDTO> toggleAvailability(@PathVariable Long id) {
    toggleCommand.execute(id);

    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);
    Optional<Equipment> equipment = getByIdCommand.execute(id);

    return equipment
        .map(e -> ResponseEntity.ok(equipmentMapper.toDto(e)))
        .orElse(ResponseEntity.notFound().build());
  }
}