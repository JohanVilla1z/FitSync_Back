package com.johan.gym_control.controllers.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentRequestDTO;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.GetEquipmentByIdCommand;
import com.johan.gym_control.services.equipment.UpdateEquipmentCommand;
import com.johan.gym_control.utils.EquipmentMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
public class UpdateEquipmentController {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  public UpdateEquipmentController(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
  }

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
}