package com.johan.gym_control.models.dto.entrylog;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEntryLogEquipmentRequest {
  @Size(max = 3, message = "No se pueden prestar más de 3 equipos simultáneamente")
  private List<Long> equipmentIds;
}