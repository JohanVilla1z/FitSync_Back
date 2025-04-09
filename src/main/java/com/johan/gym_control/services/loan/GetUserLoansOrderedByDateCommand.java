package com.johan.gym_control.services.loan;

import java.util.List;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Loan;
import com.johan.gym_control.repositories.LoanRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserLoansOrderedByDateCommand implements ICommandParametrized<List<Loan>, Long> {
  private final LoanRepository loanRepository;

  @Override
  public List<Loan> execute(Long userId) {
    return loanRepository.findByUserIdOrderByLoanDateDesc(userId);
  }
}