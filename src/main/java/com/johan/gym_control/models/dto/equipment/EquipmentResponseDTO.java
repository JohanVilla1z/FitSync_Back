package com.johan.gym_control.models.dto.equipment;

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
  private Boolean available;
  private Integer currentLoans;
}