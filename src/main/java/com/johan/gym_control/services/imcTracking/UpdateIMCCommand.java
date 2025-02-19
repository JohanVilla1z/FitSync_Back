package com.johan.gym_control.services.imcTracking;

import com.johan.gym_control.models.IMCTracking;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.IMCTrackingRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.Date;

public class UpdateIMCCommand implements ICommandParametrized<Void, User> {
  private final IMCTrackingRepository imcTrackingRepository;

  public UpdateIMCCommand(IMCTrackingRepository imcTrackingRepository) {
    this.imcTrackingRepository = imcTrackingRepository;
  }

  @Override
  public Void execute(User user) {
    Float imc = user.getUserWeight() / (user.getUserHeight() * user.getUserHeight());
    IMCTracking imcRecord = new IMCTracking();
    imcRecord.setUser(user);
    imcRecord.setMeasurementDate(new Date());
    imcRecord.setImcValue(imc);
    imcTrackingRepository.save(imcRecord);
    return null;
  }
}
