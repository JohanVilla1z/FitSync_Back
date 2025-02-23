package com.johan.gym_control.services.imcTracking;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.johan.gym_control.models.IMCTracking;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.IMCTrackingRepository;

@Service
public class UpdateIMCCommand {
  private final IMCTrackingRepository imcTrackingRepository;

  public UpdateIMCCommand(IMCTrackingRepository imcTrackingRepository) {
    this.imcTrackingRepository = imcTrackingRepository;
  }

  public void execute(User user) {
    if (user.getUserWeight() == null || user.getUserHeight() == null ||
        user.getUserWeight() <= 0 || user.getUserHeight() <= 0) {
      throw new IllegalArgumentException("Peso y altura deben ser valores positivos.");
    }

    Float heightInMeters = user.getUserHeight();
    if (user.getUserHeight() > 10) {
      heightInMeters = user.getUserHeight() / 100;
    }

    Float imcValue = Math.round((user.getUserWeight() / (heightInMeters * heightInMeters)) * 100.0f) / 100.0f;

    IMCTracking imcTracking = new IMCTracking();
    imcTracking.setUser(user);
    imcTracking.setMeasurementDate(new Date());
    imcTracking.setImcValue(imcValue);

    imcTrackingRepository.save(imcTracking);
  }
}
