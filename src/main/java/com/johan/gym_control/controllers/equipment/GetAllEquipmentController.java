package com.johan.gym_control.controllers.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.GetAllEquipmentCommand;
import com.johan.gym_control.utils.EquipmentMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
public class GetAllEquipmentController {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  public GetAllEquipmentController(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
  }

  @GetMapping
  public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
    GetAllEquipmentCommand getAllCommand = new GetAllEquipmentCommand(equipmentRepository);
    List<Equipment> equipments = getAllCommand.execute();
    List<EquipmentResponseDTO> equipmentDTOs = equipments.stream()
            .map(equipmentMapper::toDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok(equipmentDTOs);
  }
}