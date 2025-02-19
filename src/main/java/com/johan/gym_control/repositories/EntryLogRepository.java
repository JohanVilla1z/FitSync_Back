package com.johan.gym_control.repositories;

import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {
    List<EntryLog> findByUser(User user);
}
