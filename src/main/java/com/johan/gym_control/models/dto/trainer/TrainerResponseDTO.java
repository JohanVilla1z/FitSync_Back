package com.johan.gym_control.models.dto.trainer;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainerResponseDTO {
  private Long id;
  private String name;
  private String email;
  private boolean isActive;
  private boolean isAvailable;
  private List<Long> userIds;
}