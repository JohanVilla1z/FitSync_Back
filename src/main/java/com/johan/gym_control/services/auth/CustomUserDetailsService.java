package com.johan.gym_control.services.auth;

import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.enums.Role;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final AdminRepository adminRepository;
  private final TrainerRepository trainerRepository; // Add this

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Check UserRepository
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return buildUserDetails(user.getEmail(), user.getPassword(), Role.USER, user.getIsActive());
    }

    // Check AdminRepository
    Optional<Admin> adminOptional = adminRepository.findByEmail(email);
    if (adminOptional.isPresent()) {
      Admin admin = adminOptional.get();
      return buildUserDetails(admin.getEmail(), admin.getPassword(), Role.ADMIN, true);
    }

    // Check TrainerRepository
    Optional<Trainer> trainerOptional = trainerRepository.findByEmail(email);
    if (trainerOptional.isPresent()) {
      Trainer trainer = trainerOptional.get();
      return buildUserDetails(trainer.getEmail(), trainer.getPassword(), Role.TRAINER, trainer.getIsActive());
    }

    throw new UsernameNotFoundException("User not found");
  }

  private UserDetails buildUserDetails(String email, String password, Role role, Boolean isActive) {
    if (!isActive) {
      throw new DisabledException("User is disabled");
    }

    List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(role.name())
    );

    // Use the fully qualified name for Spring Security's User class
    return new org.springframework.security.core.userdetails.User(email, password, authorities);
  }
}