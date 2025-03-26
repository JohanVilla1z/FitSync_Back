package com.johan.gym_control.services.user;

import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.user.UpdatePasswordRequest;
import com.johan.gym_control.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUserPasswordCommand {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void execute(String userEmail, UpdatePasswordRequest request) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }
}