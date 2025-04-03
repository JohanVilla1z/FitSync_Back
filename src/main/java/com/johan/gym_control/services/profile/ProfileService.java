package com.johan.gym_control.services.profile;

import org.springframework.stereotype.Service;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.admin.AdminProfileResponse;
import com.johan.gym_control.models.dto.trainer.TrainerProfileResponse;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.utils.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;
  private final TrainerRepository trainerRepository;
  private final AdminRepository adminRepository;
  private final UserMapper userMapper; // Inyectamos el UserMapper

  public UserProfileResponse getUserProfile(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Usuario no encontrado con email: " + email));

    // Podemos usar directamente el mapper
    return userMapper.convertToDTO(user);
  }

  public TrainerProfileResponse getTrainerProfile(String email) {
    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Entrenador no encontrado con email: " + email));

    // Usamos el mapper para convertir la lista de usuarios
    return TrainerProfileResponse.builder()
        .id(trainer.getId())
        .name(trainer.getName())
        .email(trainer.getEmail())
        .isActive(trainer.getIsActive())
        .isAvailable(trainer.isTrainerAvailable())
        .users(userMapper.toUserSimpleDTOList(trainer.getUsers())) // Usamos el mapper
        .build();
  }

  public AdminProfileResponse getAdminProfile(String email) {
    Admin admin = adminRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Administrador no encontrado con email: " + email));

    return AdminProfileResponse.builder()
        .id(admin.getId())
        .name(admin.getName())
        .email(admin.getEmail())
        .build();
  }
}