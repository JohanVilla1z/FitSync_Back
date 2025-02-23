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
    Float imcValue = user.getUserWeight() / (user.getUserHeight() * user.getUserHeight());
    IMCTracking imcTracking = new IMCTracking();
    imcTracking.setUser(user);
    imcTracking.setMeasurementDate(new Date());
    imcTracking.setImcValue(imcValue);
    imcTrackingRepository.save(imcTracking);
  }
}