package com.johan.gym_control.controllers.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.GetEquipmentByIdCommand;
import com.johan.gym_control.utils.EquipmentMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
public class GetEquipmentByIdController {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  public GetEquipmentByIdController(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EquipmentResponseDTO> getEquipmentById(@PathVariable Long id) {
    GetEquipmentByIdCommand getByIdCommand = new GetEquipmentByIdCommand(equipmentRepository);
    Optional<Equipment> equipmentOptional = getByIdCommand.execute(id);

    return equipmentOptional
            .map(equipment -> ResponseEntity.ok(equipmentMapper.toDto(equipment)))
            .orElse(ResponseEntity.notFound().build());
  }
}