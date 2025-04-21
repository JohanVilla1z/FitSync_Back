package com.johan.gym_control.models.dto.equipment;

import com.johan.gym_control.models.enums.EquipmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponseDTO {
  private Long id;
  private String name;
  private String description;
  private EquipmentStatus status;
  private Integer loanCount;
}