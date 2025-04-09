package com.johan.gym_control.controllers.loan;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.Loan;
import com.johan.gym_control.models.dto.loan.LoanRequest;
import com.johan.gym_control.models.dto.loan.LoanResponseDTO;
import com.johan.gym_control.services.loan.CompleteLoanCommand;
import com.johan.gym_control.services.loan.CreateLoanCommand;
import com.johan.gym_control.services.loan.GetAllLoansOrderedByDateCommand;
import com.johan.gym_control.services.loan.GetUserLoansOrderedByDateCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
  private final CreateLoanCommand createLoanCommand;
  private final CompleteLoanCommand completeLoanCommand;
  private final GetAllLoansOrderedByDateCommand getAllLoansOrderedByDateCommand;
  private final GetUserLoansOrderedByDateCommand getUserLoansOrderedByDateCommand;

  @Operation(summary = "Crear un nuevo préstamo", description = "Crea un nuevo préstamo para un usuario y equipo específicos.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Préstamo creado exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json"))
  })
  @PostMapping
  public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequest loanRequest) {
    LoanResponseDTO loan = createLoanCommand.execute(loanRequest.getUserId(), loanRequest.getEquipmentId());
    return ResponseEntity.ok(loan);
  }

  @Operation(summary = "Completar un préstamo", description = "Marca un préstamo como completado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Préstamo completado exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Préstamo no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/{loanId}/complete")
  public ResponseEntity<Loan> completeLoan(@PathVariable Long loanId) {
    Loan loan = completeLoanCommand.execute(loanId);
    return ResponseEntity.ok(loan);
  }

  @Operation(summary = "Obtener todos los préstamos", description = "Obtiene todos los préstamos ordenados de más recientes a más antiguos.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente", content = @Content(mediaType = "application/json"))
  })
  @GetMapping
  public ResponseEntity<List<LoanResponseDTO>> getAllLoansOrderedByDate() {
    List<LoanResponseDTO> loans = getAllLoansOrderedByDateCommand.execute();
    return ResponseEntity.ok(loans);
  }

  @Operation(summary = "Obtener préstamos de un usuario", description = "Obtiene los préstamos de un usuario específico ordenados de más recientes a más antiguos.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Loan>> getUserLoansOrderedByDate(@PathVariable Long userId) {
    List<Loan> loans = getUserLoansOrderedByDateCommand.execute(userId);
    return ResponseEntity.ok(loans);
  }
}