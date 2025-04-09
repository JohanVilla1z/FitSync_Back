package com.johan.gym_control.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.johan.gym_control.models.Equipment;
import com.johan.gym_control.models.Loan;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.enums.LoanStatus;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
  List<Loan> findByStatus(LoanStatus status);

  List<Loan> findByUserId(Long userId);

  @Query("SELECT l FROM Loan l ORDER BY l.loanDate DESC")
  List<Loan> findAllOrderByLoanDateDesc();

  @Query("SELECT l FROM Loan l WHERE l.user.id = :userId ORDER BY l.loanDate DESC")
  List<Loan> findByUserIdOrderByLoanDateDesc(Long userId);

  boolean existsByUserAndEquipmentAndStatus(User user, Equipment equipment, LoanStatus status);
}