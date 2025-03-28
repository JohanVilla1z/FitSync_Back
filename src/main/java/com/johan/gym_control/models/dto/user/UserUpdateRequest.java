package com.johan.gym_control.models.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
  private Long id;
  private String name;
  private String lastName;
  private String email;
  private String phone;
  private Float weight;
  private Float height;
  private Boolean isActive;
}