package com.johan.gym_control.controllers.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.dto.auth.UpdatePasswordRequest;
import com.johan.gym_control.services.admin.UpdateAdminPasswordCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private final UpdateAdminPasswordCommand updateAdminPasswordCommand;

  @Operation(summary = "Actualizar contraseña del administrador autenticado", description = "Permite al administrador actualizar su contraseña.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "403", description = "Contraseña actual incorrecta", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/update-password")
  public ResponseEntity<Void> updateAdminPassword(
      @Valid @RequestBody UpdatePasswordRequest request,
      Authentication authentication) {
    String adminEmail = authentication.getName();
    updateAdminPasswordCommand.execute(adminEmail, request);
    return ResponseEntity.ok().build();
  }
}