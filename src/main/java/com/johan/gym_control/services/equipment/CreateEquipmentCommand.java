package com.johan.gym_control.services.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommand;

public class CreateEquipmentCommand implements ICommand<Equipment> {
  private final EquipmentRepository equipmentRepository;
  private final Equipment equipment;

  public CreateEquipmentCommand(EquipmentRepository equipmentRepository, Equipment equipment) {
    this.equipmentRepository = equipmentRepository;
    this.equipment = equipment;
  }

  @Override
  public Equipment execute() {
    return equipmentRepository.save(equipment);
  }
}