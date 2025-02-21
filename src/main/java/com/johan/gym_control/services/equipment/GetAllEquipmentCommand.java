package com.johan.gym_control.services.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommand;

import java.util.List;

public class GetAllEquipmentCommand implements ICommand<List<Equipment>> {
  private final EquipmentRepository equipmentRepository;

  public GetAllEquipmentCommand(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  @Override
  public List<Equipment> execute() {
    return equipmentRepository.findAll();
  }
}
