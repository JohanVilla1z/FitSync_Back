package com.johan.gym_control.controllers.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.auth.UpdatePasswordRequest;
import com.johan.gym_control.models.dto.imc_tracker.IMCHistoryDTO;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.models.dto.user.UserProfileUpdateRequest;
import com.johan.gym_control.models.dto.user.UserUpdateRequest;
import com.johan.gym_control.repositories.IMCTrackingRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.observers.IMCTrackingObserver;
import com.johan.gym_control.services.user.GetAllUsersCommand;
import com.johan.gym_control.services.user.GetUserByIdCommand;
import com.johan.gym_control.services.user.GetUserIMCHistoryCommand;
import com.johan.gym_control.services.user.GetUserProfileCommand;
import com.johan.gym_control.services.user.ToggleUserActiveStatusCommand;
import com.johan.gym_control.services.user.UpdateUserCommand;
import com.johan.gym_control.services.user.UpdateUserPasswordCommand;
import com.johan.gym_control.services.user.UpdateUserProfileCommand;
import com.johan.gym_control.services.user.UpdateUserProfileCommand.UpdateProfileParams;
import com.johan.gym_control.utils.UserMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;
  private final IMCTrackingRepository imcTrackingRepository;
  private final UserMapper userMapper;
  private final IMCTrackingObserver imcTrackingObserver;
  private final UpdateUserPasswordCommand updateUserPasswordCommand;
  private final UpdateUserCommand updateUserCommand;

  @Operation(summary = "Obtener perfil del usuario autenticado", description = "Devuelve el perfil del usuario actualmente autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
      @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/profile")
  public ResponseEntity<UserProfileResponse> getUserProfile(Authentication authentication) {
    String userEmail = authentication.getName();
    GetUserProfileCommand command = new GetUserProfileCommand(userRepository, imcTrackingRepository);
    UserProfileResponse profile = command.execute(userEmail);
    return ResponseEntity.ok(profile);
  }

  @Operation(summary = "Obtener usuario por ID", description = "Devuelve los datos de un usuario según su ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> getUserById(@PathVariable Long userId) {
    GetUserByIdCommand command = new GetUserByIdCommand(userRepository);
    Optional<User> user = command.execute(userId);
    return user.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Actualizar perfil del usuario autenticado", description = "Permite al usuario autenticado actualizar su perfil.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/profile")
  public ResponseEntity<UserProfileResponse> updateUserProfile(
      @Valid @RequestBody UserProfileUpdateRequest request,
      Authentication authentication) {
    String userEmail = authentication.getName();

    UpdateUserProfileCommand updateCommand = new UpdateUserProfileCommand(userRepository, imcTrackingObserver);
    UpdateProfileParams params = new UpdateProfileParams(userEmail, request);
    User updatedUser = updateCommand.execute(params);

    GetUserProfileCommand getCommand = new GetUserProfileCommand(userRepository, imcTrackingRepository);
    UserProfileResponse updatedProfile = getCommand.execute(updatedUser.getEmail());

    return ResponseEntity.ok(updatedProfile);
  }

  @Operation(summary = "Obtener historial de IMC del usuario autenticado", description = "Devuelve el historial completo de IMC del usuario actualmente autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Historial de IMC obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IMCHistoryDTO.class))),
      @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/imc-history")
  public ResponseEntity<List<IMCHistoryDTO>> getIMCHistory(Authentication authentication) {
    String userEmail = authentication.getName();
    GetUserIMCHistoryCommand command = new GetUserIMCHistoryCommand(userRepository, imcTrackingRepository);
    List<IMCHistoryDTO> imcHistory = command.execute(userEmail);
    return ResponseEntity.ok(imcHistory);
  }

  @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios. Solo accesible para administradores.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
      @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
    GetAllUsersCommand command = new GetAllUsersCommand(userRepository);
    List<User> users = command.execute();

    List<UserProfileResponse> userResponses = users.stream()
        .map(userMapper::convertToDTO)
        .collect(Collectors.toList());

    return ResponseEntity.ok(userResponses);
  }

  @Operation(summary = "Cambiar estado activo de un usuario", description = "Permite a un administrador activar o desactivar un usuario.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Estado del usuario cambiado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
      @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/{userId}/toggle-status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserProfileResponse> toggleUserStatus(@PathVariable Long userId) {
    ToggleUserActiveStatusCommand command = new ToggleUserActiveStatusCommand(userRepository);
    User updatedUser = command.execute(userId);

    UserProfileResponse response = UserProfileResponse.builder()
        .id(updatedUser.getId())
        .name(updatedUser.getName())
        .lastName(updatedUser.getUserLastName())
        .email(updatedUser.getEmail())
        .phone(updatedUser.getUserPhone())
        .weight(updatedUser.getUserWeight())
        .height(updatedUser.getUserHeight())
        .isActive(updatedUser.getIsActive())
        .registerDate(updatedUser.getRegisterDate())
        .build();

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Actualizar contraseña del usuario autenticado", description = "Permite al usuario autenticado actualizar su contraseña.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "403", description = "Contraseña actual incorrecta", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/update-password")
  public ResponseEntity<Void> updateUserPassword(
      @Valid @RequestBody UpdatePasswordRequest request,
      Authentication authentication) {
    String userEmail = authentication.getName();
    updateUserPasswordCommand.execute(userEmail, request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Actualizar usuario por ID", description = "Permite actualizar los datos de un usuario por su ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json"))
  })
  @PutMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> updateUser(
      @PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequest request) {
    request.setId(userId);
    try {
      User updatedUser = updateUserCommand.execute(request);
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}