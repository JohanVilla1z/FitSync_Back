package com.johan.gym_control.repositories;

import com.johan.gym_control.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
  Optional<Admin> findByAdminEmail(String email);
}
