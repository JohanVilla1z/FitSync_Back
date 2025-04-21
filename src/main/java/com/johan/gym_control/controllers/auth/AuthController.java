package com.johan.gym_control.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.johan.gym_control.exceptions.auth.AuthenticationException;
import com.johan.gym_control.models.auth.AdminRequest;
import com.johan.gym_control.models.auth.LoginRequest;
import com.johan.gym_control.models.auth.LoginResponse;
import com.johan.gym_control.models.auth.RegisterRequest;
import com.johan.gym_control.models.auth.RegisterResponse;
import com.johan.gym_control.models.error.ErrorResponse;
import com.johan.gym_control.services.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Operation(summary = "Autenticar usuario", description = "Permite a un usuario iniciar sesión con sus credenciales.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      return ResponseEntity.ok(authService.authenticate(loginRequest));
    } catch (AuthenticationException ex) {
      throw new AuthenticationException("Error en la autenticación: " + ex.getMessage());
    }
  }

  @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/register-user")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
    try {
      RegisterResponse response = authService.register(registerRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception ex) {
      ErrorResponse error = ErrorResponse.builder()
          .status(HttpStatus.BAD_REQUEST.value())
          .message("Error al registrar usuario: "
              + (ex.getMessage() != null ? ex.getMessage() : "Datos inválidos o usuario ya existe."))
          .timestamp(System.currentTimeMillis())
          .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
  }

  @Operation(summary = "Registrar administrador", description = "Registra un nuevo administrador en el sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Administrador registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/register-admin")
  public ResponseEntity<RegisterResponse> registerAdmin(@Valid @RequestBody AdminRequest adminRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdmin(adminRequest));
  }

  @Operation(summary = "Verificar disponibilidad de email", description = "Verifica si un correo electrónico ya está registrado en el sistema.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Email disponible", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "409", description = "Email ya está en uso", content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/check-email")
  public ResponseEntity<?> checkEmailAvailability(
      @Parameter(description = "Correo electrónico a verificar", required = true) @RequestParam String email) {
    boolean isEmailInUse = authService.isEmailAlreadyInUse(email);
    if (isEmailInUse) {
      ErrorResponse error = ErrorResponse.builder()
          .status(HttpStatus.CONFLICT.value())
          .message("El correo electrónico ya está en uso")
          .timestamp(System.currentTimeMillis())
          .build();
      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    return ResponseEntity.ok()
        .body("El correo electrónico está disponible");
  }
}