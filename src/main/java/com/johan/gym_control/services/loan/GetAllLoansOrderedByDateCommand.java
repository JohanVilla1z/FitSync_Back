package com.johan.gym_control.services.loan;

import java.util.List;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.Loan;
import com.johan.gym_control.models.dto.loan.LoanResponseDTO;
import com.johan.gym_control.repositories.LoanRepository;
import com.johan.gym_control.services.interfaces.ICommand;
import com.johan.gym_control.utils.LoanMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAllLoansOrderedByDateCommand implements ICommand<List<LoanResponseDTO>> {
  private final LoanRepository loanRepository;

  @Override
  public List<LoanResponseDTO> execute() {
    List<Loan> loans = loanRepository.findAllOrderByLoanDateDesc();
    return LoanMapper.toLoanResponseDTOList(loans);
  }
}