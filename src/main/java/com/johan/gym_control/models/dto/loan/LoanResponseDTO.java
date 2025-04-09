package com.johan.gym_control.models.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
  private Long id;
  private String userName;
  private String userLastName;
  private String equipmentName;
  private String loanDate;
  private String returnDate;
  private String status;
}