package com.johan.gym_control.services.equipment;

import org.springframework.transaction.annotation.Transactional;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.enums.EquipmentStatus;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class ReturnEquipmentCommand implements ICommandParametrized<Equipment, Long> {

  private final EquipmentRepository equipmentRepository;

  public ReturnEquipmentCommand(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  @Override
  @Transactional
  public Equipment execute(Long equipmentId) {
    Equipment equipment = equipmentRepository.findById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

    equipment.setEqStatus(EquipmentStatus.AVAILABLE);
    return equipmentRepository.save(equipment);
  }
}