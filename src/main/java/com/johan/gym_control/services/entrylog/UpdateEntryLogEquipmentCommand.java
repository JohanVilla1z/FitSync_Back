package com.johan.gym_control.services.entrylog;

import com.johan.gym_control.exceptions.InvalidOperationException;
import com.johan.gym_control.exceptions.InvalidStateException;
import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.repositories.EntryLogRepository;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateEntryLogEquipmentCommand implements ICommandParametrized<EntryLog, UpdateEntryLogEquipmentCommand.UpdateEquipmentParams> {

  private final EntryLogRepository entryLogRepository;
  private final EquipmentRepository equipmentRepository;

  public UpdateEntryLogEquipmentCommand(EntryLogRepository entryLogRepository, EquipmentRepository equipmentRepository) {
    this.entryLogRepository = entryLogRepository;
    this.equipmentRepository = equipmentRepository;
  }

  @Override
  @Transactional
  public EntryLog execute(UpdateEquipmentParams params) {
    EntryLog entryLog = entryLogRepository.findById(params.getEntryLogId())
            .orElseThrow(() -> new ResourceNotFoundException("Registro de entrada no encontrado"));

    // Validar tiempo límite (8 horas)
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, -8);
    Date timeLimit = calendar.getTime();

    if (entryLog.getTimestamp().before(timeLimit)) {
      throw new InvalidOperationException("No se puede modificar un registro con más de 8 horas de antigüedad");
    }

    // Validar cantidad máxima de equipos
    if (params.getEquipmentIds().size() > 3) {
      throw new InvalidOperationException("No se pueden prestar más de 3 equipos simultáneamente");
    }

    // Recopilar equipos actuales para restaurar disponibilidad
    Set<Equipment> currentEquipment = new HashSet<>(entryLog.getBorrowedEquipment());

    // Limpiar equipos actuales
    entryLog.getBorrowedEquipment().clear();

    // Agregar nuevos equipos
    Set<Equipment> newEquipment = new HashSet<>();
    for (Long eqId : params.getEquipmentIds()) {
      Equipment equipment = equipmentRepository.findById(eqId)
              .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado: " + eqId));

      // Validar disponibilidad (solo si no estaba ya prestado a este usuario)
      if (!currentEquipment.contains(equipment) && !equipment.getEqAvailable()) {
        throw new InvalidStateException("El equipo no está disponible: " + equipment.getEqName());
      }

      newEquipment.add(equipment);
      equipment.setEqAvailable(false);
      equipmentRepository.save(equipment);
    }

    // Restaurar disponibilidad de equipos que ya no están prestados
    for (Equipment equipment : currentEquipment) {
      if (!newEquipment.contains(equipment)) {
        equipment.setEqAvailable(true);
        equipmentRepository.save(equipment);
      }
    }

    entryLog.getBorrowedEquipment().addAll(newEquipment);
    return entryLogRepository.save(entryLog);
  }

  @AllArgsConstructor
  @Getter
  public static class UpdateEquipmentParams {
    private Long entryLogId;
    private List<Long> equipmentIds;
  }
}