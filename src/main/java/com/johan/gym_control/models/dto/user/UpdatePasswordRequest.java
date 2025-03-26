package com.johan.gym_control.models.dto.user;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
  private String currentPassword;
  private String newPassword;
}