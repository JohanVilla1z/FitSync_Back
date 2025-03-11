package com.johan.gym_control.services.auth;

import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email).orElse(null);

    if (user != null) {
      return org.springframework.security.core.userdetails.User
              .withUsername(user.getEmail())
              .password(user.getPassword())
              .authorities(user.getRole().name())
              .build();
    }

    Admin admin = adminRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return org.springframework.security.core.userdetails.User
            .withUsername(admin.getEmail())
            .password(admin.getPassword())
            .authorities(admin.getRole().name())
            .build();
  }
}