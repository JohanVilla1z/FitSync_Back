package com.johan.gym_control.controllers.user;

import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.imc_tracker.IMCHistoryDTO;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.models.dto.user.UserProfileUpdateRequest;
import com.johan.gym_control.repositories.IMCTrackingRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.observers.IMCTrackingObserver;
import com.johan.gym_control.services.user.GetUserIMCHistoryCommand;
import com.johan.gym_control.services.user.GetUserProfileCommand;
import com.johan.gym_control.services.user.UpdateUserProfileCommand;
import com.johan.gym_control.services.user.UpdateUserProfileCommand.UpdateProfileParams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;
  private final IMCTrackingRepository imcTrackingRepository;
  private final IMCTrackingObserver imcTrackingObserver;

  /**
   * Endpoint para obtener el perfil del usuario autenticado
   */
  @GetMapping("/profile")
  public ResponseEntity<UserProfileResponse> getUserProfile(Authentication authentication) {
    String userEmail = authentication.getName();
    GetUserProfileCommand command = new GetUserProfileCommand(userRepository, imcTrackingRepository);
    UserProfileResponse profile = command.execute(userEmail);
    return ResponseEntity.ok(profile);
  }

  /**
   * Endpoint para actualizar el perfil del usuario autenticado
   */
  @PutMapping("/profile")
  public ResponseEntity<UserProfileResponse> updateUserProfile(
          @Valid @RequestBody UserProfileUpdateRequest request,
          Authentication authentication) {
    String userEmail = authentication.getName();

    // Actualizar el perfil
    UpdateUserProfileCommand updateCommand = new UpdateUserProfileCommand(userRepository, imcTrackingObserver);
    UpdateProfileParams params = new UpdateProfileParams(userEmail, request);
    User updatedUser = updateCommand.execute(params);

    // Obtener el perfil actualizado
    GetUserProfileCommand getCommand = new GetUserProfileCommand(userRepository, imcTrackingRepository);
    UserProfileResponse updatedProfile = getCommand.execute(updatedUser.getEmail());

    return ResponseEntity.ok(updatedProfile);
  }

  /**
   * Endpoint para obtener el historial completo de IMC del usuario autenticado
   */
  @GetMapping("/imc-history")
  public ResponseEntity<List<IMCHistoryDTO>> getIMCHistory(Authentication authentication) {
    String userEmail = authentication.getName();
    GetUserIMCHistoryCommand command = new GetUserIMCHistoryCommand(userRepository, imcTrackingRepository);
    List<IMCHistoryDTO> imcHistory = command.execute(userEmail);
    return ResponseEntity.ok(imcHistory);
  }
}