package com.johan.gym_control.utils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.johan.gym_control.models.Loan;
import com.johan.gym_control.models.dto.loan.LoanResponseDTO;

public class LoanMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

  public static LoanResponseDTO toLoanResponseDTO(Loan loan) {
    return LoanResponseDTO.builder()
        .id(loan.getId())
        .userName(loan.getUser().getName())
        .userLastName(loan.getUser().getUserLastName())
        .equipmentName(loan.getEquipment().getEqName())
        .loanDate(loan.getLoanDate().format(DATE_FORMATTER))
        .returnDate(loan.getReturnDate() != null ? loan.getReturnDate().format(DATE_FORMATTER) : null)
        .status(loan.getStatus().name())
        .build();
  }

  public static List<LoanResponseDTO> toLoanResponseDTOList(List<Loan> loans) {
    return loans.stream()
        .map(LoanMapper::toLoanResponseDTO)
        .collect(Collectors.toList());
  }
}