package com.johan.gym_control.services.equipment;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.enums.EquipmentStatus;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

@Service
public class ToggleEquipmentAvailabilityCommand implements ICommandParametrized<Void, Long> {
    private final EquipmentRepository equipmentRepository;

    public ToggleEquipmentAvailabilityCommand(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Void execute(Long equipmentId) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            if (equipment.getEqStatus() == EquipmentStatus.AVAILABLE) {
                equipment.setEqStatus(EquipmentStatus.UNAVAILABLE);
            } else {
                equipment.setEqStatus(EquipmentStatus.AVAILABLE);
            }
            equipmentRepository.save(equipment);
        }
        return null;
    }
}