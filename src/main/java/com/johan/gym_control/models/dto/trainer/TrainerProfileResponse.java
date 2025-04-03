package com.johan.gym_control.models.dto.trainer;

import java.util.List;

import com.johan.gym_control.models.dto.user.UserSimpleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileResponse {
  private Long id;
  private String name;
  private String email;
  private Boolean isActive;
  private Boolean isAvailable;
  private List<UserSimpleDTO> users;
}