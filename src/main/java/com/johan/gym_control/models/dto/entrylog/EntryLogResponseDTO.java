package com.johan.gym_control.models.dto.entrylog;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryLogResponseDTO {
  private Long logId;
  private Date timestamp;
  private String userName;
  private String userLastName;
  private Boolean editable; // Indica si han pasado menos de 8 horas
}