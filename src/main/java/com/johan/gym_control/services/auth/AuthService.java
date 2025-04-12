package com.johan.gym_control.services.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johan.gym_control.config.security.JwtTokenProvider;
import com.johan.gym_control.exceptions.auth.AuthenticationException;
import com.johan.gym_control.exceptions.auth.UserAlreadyExistsException;
import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.auth.AdminRequest;
import com.johan.gym_control.models.auth.LoginRequest;
import com.johan.gym_control.models.auth.LoginResponse;
import com.johan.gym_control.models.auth.RegisterRequest;
import com.johan.gym_control.models.auth.RegisterResponse;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.observers.IMCTrackingObserver;
import com.johan.gym_control.services.user.CreateUserCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;
  private final CustomUserDetailsService userDetailsService;
  private final IMCTrackingObserver imcTrackingObserver;

  public RegisterResponse register(RegisterRequest request) {
    // Check if user already exists
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException("El usuario con email " + request.getEmail() + " ya existe");
    }

    // Create new user
    User user = new User();
    user.setName(request.getName());
    user.setUserLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setUserHeight(request.getUserHeight());
    user.setUserWeight(request.getUserWeight());
    user.setIsActive(true);
    user.setRegisterDate(new java.util.Date());

    // Use the Command pattern
    CreateUserCommand createUserCommand = new CreateUserCommand(userRepository, user, imcTrackingObserver);
    User savedUser = createUserCommand.execute();

    // Return response
    return RegisterResponse.builder().email(savedUser.getEmail()).name(savedUser.getName())
        .message("Usuario registrado exitosamente").build();
  }

  public RegisterResponse registerAdmin(AdminRequest request) {
    // Check if admin already exists
    if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException("El administrador con email " + request.getEmail() + " ya existe");
    }

    // Create new admin
    Admin admin = new Admin();
    admin.setName(request.getName() + " " + request.getLastName());
    admin.setEmail(request.getEmail());
    admin.setPassword(passwordEncoder.encode(request.getPassword()));

    // Save admin
    Admin savedAdmin = adminRepository.save(admin);

    // Return response
    return RegisterResponse.builder().email(savedAdmin.getEmail()).name(savedAdmin.getName())
        .message("Administrador registrado exitosamente").build();
  }

  public LoginResponse authenticate(LoginRequest request) {
    try {
      // Validate user exists before authentication
      UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

      // Generate JWT token
      String jwt = tokenProvider.generateToken(userDetails);

      // Build and return response
      return LoginResponse.builder().token(jwt).type("Bearer").email(userDetails.getUsername())
          .role(userDetails.getAuthorities().iterator().next().getAuthority()).build();

    } catch (AuthenticationException e) {
      throw new com.johan.gym_control.exceptions.auth.AuthenticationException(
          "Credenciales inválidas: " + e.getMessage());
    } catch (Exception e) {
      throw new com.johan.gym_control.exceptions.auth.AuthenticationException(
          "Error en la autenticación: " + e.getMessage());
    }
  }

  /**
   * Verifica si un correo electrónico ya está registrado en el sistema.
   *
   * @param email Correo electrónico a verificar
   * @return true si el email ya está en uso, false si está disponible
   * @throws IllegalArgumentException si el email es nulo o vacío
   */
  public boolean isEmailAlreadyInUse(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("El correo electrónico no puede estar vacío");
    }

    boolean userExists = userRepository.findByEmail(email).isPresent();

    boolean adminExists = adminRepository.findByEmail(email).isPresent();

    return userExists || adminExists;
  }
}