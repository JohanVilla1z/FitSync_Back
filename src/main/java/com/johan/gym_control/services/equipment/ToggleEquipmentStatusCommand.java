package com.johan.gym_control.services.equipment;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.enums.EquipmentStatus;
import com.johan.gym_control.repositories.EquipmentRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service to toggle equipment status following predefined transition rules.
 */
@Service
@RequiredArgsConstructor
public class ToggleEquipmentStatusCommand {
    private final EquipmentRepository equipmentRepository;

    private static final Map<EquipmentStatus, EquipmentStatus> STATUS_TRANSITIONS = Map.of(
            EquipmentStatus.AVAILABLE, EquipmentStatus.UNAVAILABLE,
            EquipmentStatus.UNAVAILABLE, EquipmentStatus.AVAILABLE,
            EquipmentStatus.LOANED, EquipmentStatus.UNAVAILABLE);

    /**
     * Toggles equipment status based on predefined transition rules.
     *
     * @param equipmentId The ID of the equipment to update
     * @throws ResourceNotFoundException if equipment is not found
     * @throws IllegalStateException     if current status has no defined transition
     */
    @Transactional
    public void execute(Long equipmentId) {
        if (equipmentId == null) {
            throw new IllegalArgumentException("Equipment ID cannot be null");
        }

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + equipmentId));

        EquipmentStatus currentStatus = equipment.getEqStatus();
        EquipmentStatus nextStatus = Optional.ofNullable(STATUS_TRANSITIONS.get(currentStatus))
                .orElseThrow(() -> new IllegalStateException(
                        "No transition defined for status: " + currentStatus));

        equipment.setEqStatus(nextStatus);
        equipmentRepository.save(equipment);
    }
}