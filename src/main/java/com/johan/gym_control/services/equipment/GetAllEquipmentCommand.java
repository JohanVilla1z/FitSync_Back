package com.johan.gym_control.services.equipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommand;

public class GetAllEquipmentCommand implements ICommand<Page<Equipment>> {
  private final EquipmentRepository equipmentRepository;
  private final Pageable pageable;

  public GetAllEquipmentCommand(EquipmentRepository equipmentRepository, Pageable pageable) {
    this.equipmentRepository = equipmentRepository;
    this.pageable = pageable;
  }

  @Override
  public Page<Equipment> execute() {
    return equipmentRepository.findAll(pageable);
  }
}
