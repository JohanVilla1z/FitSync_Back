package com.johan.gym_control.services.entrylog;

import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.User;
import com.johan.gym_control.repositories.EntryLogRepository;
import com.johan.gym_control.services.interfaces.ICommandParametrized;

import java.util.List;

public class GetUserEntryLogsCommand implements ICommandParametrized<List<EntryLog>, User> {

  private final EntryLogRepository entryLogRepository;

  public GetUserEntryLogsCommand(EntryLogRepository entryLogRepository) {
    this.entryLogRepository = entryLogRepository;
  }

  @Override
  public List<EntryLog> execute(User user) {
    return entryLogRepository.findByUserOrderByTimestampDesc(user);
  }
}