package com.johan.gym_control.controllers.equipment;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {
  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;
  private final ToggleEquipmentAvailabilityCommand toggleCommand;

  /**
   * Crear un nuevo equipo
   */
  @PostMapping
  public ResponseEntity<EquipmentResponseDTO> createEquipment(
          @Valid @RequestBody EquipmentRequestDTO equipmentRequestDTO) {
    Equipment equipment = equipmentMapper.toEntity(equipmentRequestDTO);
    CreateEquipmentCommand createCommand = new CreateEquipmentCommand(equipmentRepository, equipment);
    Equipment savedEquipment = createCommand.execute();
    return new ResponseEntity<>(equipmentMapper.toDto(savedEquipment), HttpStatus.CREATED);
  }

  /**
   * Obtener todos los equipos
   */
  @GetMapping
  public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
    GetAllEquipmentCommand getAllCommand = new GetAllEquipmentCommand(equipmentRepository);
    List<Equipment> equipments = getAllCommand.execute();
    List<EquipmentResponseDTO> equipmentDTOs = equipments.stream()
            .map(equipmentMapper::toDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok(equipmentDTOs);
  }

  /**
   * Obtener un equipo por su ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<EquipmentResponseDTO> getEquipmentById(@PathVariable Long id) {
    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);
    Optional<Equipment> equipmentOptional = getByIdCommand.execute(id);

    return equipmentOptional
            .map(equipment -> ResponseEntity.ok(equipmentMapper.toDto(equipment)))
            .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Actualizar un equipo existente
   */
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

  /**
   * Eliminar un equipo
   */
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

  /**
   * Cambiar la disponibilidad de un equipo
   */
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