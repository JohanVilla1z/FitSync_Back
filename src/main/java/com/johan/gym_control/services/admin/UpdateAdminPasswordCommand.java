package com.johan.gym_control.services.admin;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.dto.auth.UpdatePasswordRequest;
import com.johan.gym_control.repositories.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateAdminPasswordCommand {
  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void execute(String email, UpdatePasswordRequest request) {
    // Validar que las contraseñas nuevas coincidan
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("Las contraseñas no coinciden");
    }

    Admin admin = adminRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con email: " + email));

    // Verificar contraseña actual
    if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
      throw new AccessDeniedException("La contraseña actual es incorrecta");
    }

    // Actualizar contraseña
    admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
    adminRepository.save(admin);
  }
}