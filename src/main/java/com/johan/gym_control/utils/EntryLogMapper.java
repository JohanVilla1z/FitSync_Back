package com.johan.gym_control.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.dto.entrylog.EntryLogResponseDTO;

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

    return EntryLogResponseDTO.builder()
        .logId(entryLog.getLogId())
        .timestamp(entryLog.getTimestamp())
        .userName(entryLog.getUser().getName())
        .userLastName(entryLog.getUser().getUserLastName())
        .editable(isEditable)
        .build();
  }
}