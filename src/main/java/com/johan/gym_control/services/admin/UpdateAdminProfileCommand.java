package com.johan.gym_control.services.admin;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.dto.admin.AdminProfileResponse;
import com.johan.gym_control.models.dto.admin.AdminProfileUpdateRequest;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UpdateAdminProfileCommand
    implements ICommandParametrized<AdminProfileResponse, UpdateAdminProfileCommand.UpdateAdminProfileParams> {

  private final AdminRepository adminRepository;

  public UpdateAdminProfileCommand(AdminRepository adminRepository) {
    this.adminRepository = adminRepository;
  }

  @Override
  public AdminProfileResponse execute(UpdateAdminProfileParams params) {
    Admin admin = adminRepository.findByEmail(params.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Administrador no encontrado con email: " + params.getEmail()));

    // Actualizar campos
    admin.setName(params.getRequest().getName());
    admin.setEmail(params.getRequest().getEmail());

    // Guardar y convertir a DTO para respuesta
    Admin updatedAdmin = adminRepository.save(admin);

    return AdminProfileResponse.builder()
        .id(updatedAdmin.getId())
        .name(updatedAdmin.getName())
        .email(updatedAdmin.getEmail())
        .build();
  }

  @AllArgsConstructor
  @Getter
  public static class UpdateAdminProfileParams {
    private String email;
    private AdminProfileUpdateRequest request;
  }
}