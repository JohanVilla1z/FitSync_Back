package com.johan.gym_control.models.dto.loan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanRequest {
  private Long userId;
  private Long equipmentId;
}