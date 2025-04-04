package com.johan.gym_control.controllers.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.dto.admin.AdminProfileResponse;
import com.johan.gym_control.models.dto.admin.AdminProfileUpdateRequest;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.services.admin.UpdateAdminProfileCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

  private final AdminRepository adminRepository;

  @Operation(summary = "Actualizar perfil de administrador")
  @ApiResponse(responseCode = "200", description = "Perfil actualizado correctamente")
  @ApiResponse(responseCode = "400", description = "Datos inválidos")
  @ApiResponse(responseCode = "404", description = "Administrador no encontrado")
  @PutMapping
  public ResponseEntity<AdminProfileResponse> updateAdminProfile(
      Authentication authentication,
      @Valid @RequestBody AdminProfileUpdateRequest request) {

    String email = authentication.getName();

    // Crear directamente el comando y ejecutarlo
    UpdateAdminProfileCommand command = new UpdateAdminProfileCommand(adminRepository);
    AdminProfileResponse response = command.execute(
        new UpdateAdminProfileCommand.UpdateAdminProfileParams(email, request));

    return ResponseEntity.ok(response);
  }
}