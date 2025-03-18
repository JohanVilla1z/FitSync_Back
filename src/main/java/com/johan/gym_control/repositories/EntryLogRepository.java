package com.johan.gym_control.repositories;

import com.johan.gym_control.models.EntryLog;
import com.johan.gym_control.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {
  List<EntryLog> findByUserOrderByTimestampDesc(User user);

  @Query("SELECT e FROM EntryLog e WHERE e.user = :user AND e.timestamp >= :timeLimit ORDER BY e.timestamp DESC")
  List<EntryLog> findRecentByUser(@Param("user") User user, @Param("timeLimit") Date timeLimit);

  Optional<EntryLog> findFirstByUserOrderByTimestampDesc(User user);

  List<EntryLog> findAllByOrderByTimestampDesc();
}
