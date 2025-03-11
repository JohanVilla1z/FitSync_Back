package com.johan.gym_control.controllers.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.GetEquipmentByIdCommand;
import com.johan.gym_control.services.equipment.ToggleEquipmentAvailabilityCommand;
import com.johan.gym_control.utils.EquipmentMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
public class ToggleEquipmentAvailabilityController {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;
  private final ToggleEquipmentAvailabilityCommand toggleCommand;

  public ToggleEquipmentAvailabilityController(
          EquipmentRepository equipmentRepository,
          EquipmentMapper equipmentMapper,
          ToggleEquipmentAvailabilityCommand toggleCommand) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
    this.toggleCommand = toggleCommand;
  }

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