package com.johan.gym_control.services.entrylog;

import java.util.Date;

import com.johan.gym_control.exceptions.InvalidStateException;
import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.EntryLogRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

public class CreateEntryLogCommand implements ICommandParametrized<EntryLog, User> {

  private final EntryLogRepository entryLogRepository;

  public CreateEntryLogCommand(EntryLogRepository entryLogRepository) {
    this.entryLogRepository = entryLogRepository;
  }

  @Override
  public EntryLog execute(User user) {
    // Validar que el usuario esté activo
    if (!user.getIsActive()) {
      throw new InvalidStateException("El usuario no está activo y no puede registrar entrada");
    }

    EntryLog entryLog = new EntryLog();
    entryLog.setUser(user);
    entryLog.setTimestamp(new Date());

    return entryLogRepository.save(entryLog);
  }
}