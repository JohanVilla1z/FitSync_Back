package com.johan.gym_control.services.observers;

import org.springframework.stereotype.Component;

import com.johan.gym_control.models.User;
import com.johan.gym_control.services.imcTracking.UpdateIMCCommand;
import com.johan.gym_control.services.observers.interfaces.IMCObserver;

@Component
public class IMCTrackingObserver implements IMCObserver {
  private final UpdateIMCCommand updateIMCCommand;

  public IMCTrackingObserver(UpdateIMCCommand updateIMCCommand) {
    this.updateIMCCommand = updateIMCCommand;
  }

  @Override
  public void updateIMC(User user) {
    try {
      if (user.getUserWeight() != null && user.getUserHeight() != null &&
          user.getUserWeight() > 0 && user.getUserHeight() > 0) {
        updateIMCCommand.execute(user);
      }
    } catch (Exception e) {
      // Aquí podrías agregar logging o manejo de errores
      throw new RuntimeException("Error al actualizar el IMC: " + e.getMessage());
    }
  }
}