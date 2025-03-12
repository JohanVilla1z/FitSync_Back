package com.johan.gym_control.services.user;

import java.util.List;
import java.util.stream.Collectors;

import com.johan.gym_control.exceptions.ResourceNotFoundException;
import com.johan.gym_control.models.IMCTracking;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.dto.imc_tracker.IMCHistoryDTO;
import com.johan.gym_control.repositories.IMCTrackingRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class GetUserIMCHistoryCommand implements ICommandParametrized<List<IMCHistoryDTO>, String> {
  private final UserRepository userRepository;
  private final IMCTrackingRepository imcTrackingRepository;

  public GetUserIMCHistoryCommand(UserRepository userRepository, IMCTrackingRepository imcTrackingRepository) {
    this.userRepository = userRepository;
    this.imcTrackingRepository = imcTrackingRepository;
  }

  @Override
  public List<IMCHistoryDTO> execute(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

    List<IMCTracking> imcHistory = imcTrackingRepository.findByUserOrderByMeasurementDateDesc(user);

    return imcHistory.stream()
            .map(tracking -> IMCHistoryDTO.builder()
                    .id(tracking.getTrackId())
                    .measurementDate(tracking.getMeasurementDate())
                    .imcValue(tracking.getImcValue())
                    .userHeight(tracking.getUser().getUserWeight())
                    .userWeight(tracking.getUser().getUserHeight())
                    .build())
            .collect(Collectors.toList());
  }
}