package com.johan.gym_control.services.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class UpdateEquipmentCommand implements ICommandParametrized<Equipment, Equipment> {
  private final EquipmentRepository equipmentRepository;

  public UpdateEquipmentCommand(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  @Override
  public Equipment execute(Equipment equipment) {
    return equipmentRepository.save(equipment);
  }
}
