package com.johan.gym_control.controllers.profile;

import com.johan.gym_control.models.dto.admin.AdminProfileResponse;
import com.johan.gym_control.models.dto.trainer.TrainerProfileResponse;
import com.johan.gym_control.models.dto.user.UserProfileResponse;
import com.johan.gym_control.services.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @Operation(summary = "Obtener perfil del usuario autenticado",
          description = "Devuelve la información del perfil basado en el rol del usuario (usuario, entrenador o administrador)")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
                  content = {
                          @Content(mediaType = "application/json",
                                  schema = @Schema(oneOf = {
                                          UserProfileResponse.class,
                                          TrainerProfileResponse.class,
                                          AdminProfileResponse.class
                                  }))
                  }),
          @ApiResponse(responseCode = "403", description = "No autorizado"),
          @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping
  public ResponseEntity<?> getCurrentUserProfile(Authentication authentication) {
    String email = authentication.getName();
    String role = authentication.getAuthorities().iterator().next().getAuthority();

    switch (role) {
      case "ROLE_USER":
        return ResponseEntity.ok(profileService.getUserProfile(email));
      case "ROLE_TRAINER":
        return ResponseEntity.ok(profileService.getTrainerProfile(email));
      case "ROLE_ADMIN":
        return ResponseEntity.ok(profileService.getAdminProfile(email));
      default:
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
}