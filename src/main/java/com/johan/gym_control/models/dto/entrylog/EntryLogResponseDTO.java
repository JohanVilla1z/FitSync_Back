package com.johan.gym_control.models.dto.entrylog;

import com.johan.gym_control.models.dto.equipment.EquipmentBorrowedDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryLogResponseDTO {
  private Long logId;
  private Date timestamp;
  private String userName;
  private Set<EquipmentBorrowedDTO> borrowedEquipment;
  private Boolean editable; // Indica si han pasado menos de 8 horas
}