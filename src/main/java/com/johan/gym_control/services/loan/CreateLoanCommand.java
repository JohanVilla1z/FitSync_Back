package com.johan.gym_control.services.loan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.Loan;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.loan.LoanResponseDTO;
import com.johan.gym_control.models.enums.LoanStatus;
import com.johan.gym_control.repositories.EquipmentRepository;
import com.johan.gym_control.repositories.LoanRepository;
import com.johan.gym_control.repositories.UserRepository;

@Service
public class CreateLoanCommand {
  private final LoanRepository loanRepository;
  private final EquipmentRepository equipmentRepository;
  private final UserRepository userRepository;

  public CreateLoanCommand(LoanRepository loanRepository, EquipmentRepository equipmentRepository,
      UserRepository userRepository) {
    this.loanRepository = loanRepository;
    this.equipmentRepository = equipmentRepository;
    this.userRepository = userRepository;
  }

  public LoanResponseDTO execute(Long userId, Long equipmentId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    Equipment equipment = equipmentRepository.findById(equipmentId)
        .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));

    if (!equipment.getEqAvailable()) {
      throw new IllegalStateException("El equipo no está disponible");
    }

    boolean hasPendingLoan = loanRepository.existsByUserAndEquipmentAndStatus(user, equipment, LoanStatus.PENDING);
    if (hasPendingLoan) {
      throw new IllegalStateException("El usuario ya tiene un préstamo pendiente para este equipo");
    }

    equipment.setEqAvailable(false);
    equipmentRepository.save(equipment);

    Loan loan = new Loan();
    loan.setUser(user);
    loan.setEquipment(equipment);
    loan.setLoanDate(LocalDateTime.now());
    loan.setStatus(LoanStatus.PENDING);

    Loan savedLoan = loanRepository.save(loan);

    return LoanResponseDTO.builder()
        .id(savedLoan.getId())
        .userName(user.getName())
        .userLastName(user.getUserLastName())
        .equipmentName(equipment.getEqName())
        .loanDate(savedLoan.getLoanDate().format(DateTimeFormatter.ISO_DATE_TIME))
        .status(savedLoan.getStatus().name())
        .build();
  }
}