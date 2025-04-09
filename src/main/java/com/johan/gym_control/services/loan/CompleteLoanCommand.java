package com.johan.gym_control.services.loan;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.Loan;
import com.johan.gym_control.models.enums.LoanStatus;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.repositories.LoanRepository;

@Service
public class CompleteLoanCommand {
  private final LoanRepository loanRepository;
  private final EquipmentRepository equipmentRepository;

  public CompleteLoanCommand(LoanRepository loanRepository, EquipmentRepository equipmentRepository) {
    this.loanRepository = loanRepository;
    this.equipmentRepository = equipmentRepository;
  }

  public Loan execute(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

    if (loan.getStatus() == LoanStatus.RETURNED) {
      throw new IllegalStateException("El préstamo ya fue completado");
    }

    loan.setStatus(LoanStatus.RETURNED);
    loan.setReturnDate(LocalDateTime.now());

    Equipment equipment = loan.getEquipment();
    equipment.setEqAvailable(true);
    equipmentRepository.save(equipment);

    return loanRepository.save(loan);
  }
}