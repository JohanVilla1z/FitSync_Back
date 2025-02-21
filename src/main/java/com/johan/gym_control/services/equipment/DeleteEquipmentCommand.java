package com.johan.gym_control.services.equipment;

import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class DeleteEquipmentCommand implements ICommandParametrized<Void, Long> {
  private final EquipmentRepository equipmentRepository;

  public DeleteEquipmentCommand(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  @Override
  public Void execute(Long id) {
    equipmentRepository.deleteById(id);
    return null;
  }
}