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
    // Validar que peso y altura no sean nulos o cero
    if (user.getUserWeight() == null || user.getUserHeight() == null ||
        user.getUserWeight() <= 0 || user.getUserHeight() <= 0) {
      throw new IllegalArgumentException("Peso y altura deben ser valores positivos");
    }

    // Convertir altura a metros si está en centímetros
    Float heightInMeters = user.getUserHeight() >= 3 ? user.getUserHeight() / 100 : user.getUserHeight();

    // Calcular IMC con 2 decimales
    Float imcValue = Math.round((user.getUserWeight() / (heightInMeters * heightInMeters)) * 100.0f) / 100.0f;

    IMCTracking imcTracking = new IMCTracking();
    imcTracking.setUser(user);
    imcTracking.setMeasurementDate(new Date());
    imcTracking.setImcValue(imcValue);

    imcTrackingRepository.save(imcTracking);
  }
}