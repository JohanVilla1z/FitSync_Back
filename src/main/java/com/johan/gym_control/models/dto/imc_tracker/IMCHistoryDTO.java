package com.johan.gym_control.models.dto.imc_tracker;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IMCHistoryDTO {
  private Long id;
  private Date measurementDate;
  private Float imcValue;
  private Float userHeight;
  private Float userWeight;
}