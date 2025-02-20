package com.johan.gym_control.services.observers;

import com.johan.gym_control.models.User;
import com.johan.gym_control.services.imcTracking.UpdateIMCCommand;
import com.johan.gym_control.services.observers.interfaces.IMCObserver;
import org.springframework.stereotype.Component;

@Component
public class IMCTrackingObserver implements IMCObserver {
    private final UpdateIMCCommand updateIMCCommand;

    public IMCTrackingObserver(UpdateIMCCommand updateIMCCommand) {
        this.updateIMCCommand = updateIMCCommand;
    }

    @Override
    public void updateIMC(User user) {
        if (user.getUserWeight() != null && user.getUserHeight() != null) {
            updateIMCCommand.execute(user);
        }
    }
}