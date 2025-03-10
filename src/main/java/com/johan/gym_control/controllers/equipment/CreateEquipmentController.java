package com.johan.gym_control.controllers.equipment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.dto.equipment.EquipmentRequestDTO;
import com.johan.gym_control.models.dto.equipment.EquipmentResponseDTO;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.equipment.CreateEquipmentCommand;
import com.johan.gym_control.utils.EquipmentMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/equipment")
public class CreateEquipmentController {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  public CreateEquipmentController(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
    this.equipmentRepository = equipmentRepository;
    this.equipmentMapper = equipmentMapper;
  }

  @PostMapping
  public ResponseEntity<EquipmentResponseDTO> createEquipment(
      @Valid @RequestBody EquipmentRequestDTO equipmentRequestDTO) {
    Equipment equipment = equipmentMapper.toEntity(equipmentRequestDTO);
    CreateEquipmentCommand createCommand = new CreateEquipmentCommand(equipmentRepository, equipment);
    Equipment savedEquipment = createCommand.execute();
    return new ResponseEntity<>(equipmentMapper.toDto(savedEquipment), HttpStatus.CREATED);
  }
}