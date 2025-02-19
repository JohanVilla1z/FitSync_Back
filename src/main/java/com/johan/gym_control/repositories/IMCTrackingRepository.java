package com.johan.gym_control.repositories;

import com.johan.gym_control.models.IMCTracking;
import com.johan.gym_control.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMCTrackingRepository extends JpaRepository<IMCTracking, Long> {
    List<IMCTracking> findByUser(User user);
}
