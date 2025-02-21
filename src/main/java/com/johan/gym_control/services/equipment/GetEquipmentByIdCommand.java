package com.johan.gym_control.services.equipment;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.Optional;

public class GetEquipmentByIdCommand implements ICommandParametrized<Optional<Equipment>, Long> {
  private final EquipmentRepository equipmentRepository;

  public GetEquipmentByIdCommand(EquipmentRepository equipmentRepository) {
    this.equipmentRepository = equipmentRepository;
  }

  @Override
  public Optional<Equipment> execute(Long id) {
    return equipmentRepository.findById(id);
  }
}
