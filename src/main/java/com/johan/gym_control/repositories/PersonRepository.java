package com.johan.gym_control.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johan.gym_control.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  Optional<Person> findByEmail(String email);
}
