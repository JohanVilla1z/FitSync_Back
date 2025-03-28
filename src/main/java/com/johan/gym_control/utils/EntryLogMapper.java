package com.johan.gym_control.utils;

import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.dto.entrylog.EntryLogResponseDTO;
import com.johan.gym_control.models.dto.equipment.EquipmentBorrowedDTO;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntryLogMapper {

  /**
   * Convierte una entidad EntryLog a un DTO de respuesta
   *
   * @param entryLog Entidad EntryLog
   * @return DTO de respuesta
   */
  public EntryLogResponseDTO convertToDTO(EntryLog entryLog) {
    if (entryLog == null) {
      return null;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, -8);
    Date timeLimit = calendar.getTime();
    boolean isEditable = entryLog.getTimestamp().after(timeLimit);

    Set<EquipmentBorrowedDTO> equipmentDTOs = entryLog.getBorrowedEquipment().stream()
            .map(eq -> EquipmentBorrowedDTO.builder()
                    .id(eq.getEqId())
                    .name(eq.getEqName())
                    .description(eq.getEqDescription())
                    .build())
            .collect(Collectors.toSet());

    return EntryLogResponseDTO.builder()
            .logId(entryLog.getLogId())
            .timestamp(entryLog.getTimestamp())
            .userName(entryLog.getUser().getName())
            .userLastName(entryLog.getUser().getUserLastName())
            .borrowedEquipment(equipmentDTOs)
            .editable(isEditable)
            .build();
  }
}