package com.johan.gym_control.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.johan.gym_control.models.Admin;
import com.johan.gym_control.models.Trainer;
import com.johan.gym_control.models.User;
import com.johan.gym_control.models.enums.Role;
import com.johan.gym_control.repositories.AdminRepository;
import com.johan.gym_control.repositories.TrainerRepository;
import com.johan.gym_control.repositories.UserRepository;
import com.johan.gym_control.services.observers.IMCTrackingObserver;

@Component
public class DataSeeder implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

  @Autowired
  private AdminRepository adminRepository;

  @Autowired
  private TrainerRepository trainerRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private IMCTrackingObserver imcTrackingObserver;

  @Value("${seed.admin.email:admin@fitsync.com}")
  private String adminEmail;

  @Value("${seed.admin.password:Admin123!}")
  private String adminPassword;

  @Value("${seed.trainer.email:trainer@fitsync.com}")
  private String trainerEmail;

  @Value("${seed.trainer.password:Trainer123!}")
  private String trainerPassword;

  @Value("${seed.user.email:user@fitsync.com}")
  private String userEmail;

  @Value("${seed.user.password:User123!}")
  private String userPassword;

  @Value("${spring.profiles.active:default}")
  private String activeProfile;

  @Override
  public void run(String... args) {
    logger.info("DataSeeder running with active profile: {}", activeProfile);
    // Attempt seeding only if the database connection is likely available (though the root error suggests it's not)
    // Seeding logic depends on successful repository bean creation.
    if (shouldSeedData()) {
      logger.info("Attempting to seed data based on profile or environment variable.");
      try {
        seedAdminUser();
        seedTrainerUser();
        seedRegularUser();
      } catch (Exception e) {
        logger.error("Error during data seeding, likely due to DB connection issues: {}", e.getMessage());
        // Log the root cause if available, which might be more informative
        if (e.getCause() != null) {
          logger.error("Data seeding root cause: {}", e.getCause().getMessage());
        }
      }
    } else {
      logger.info("Skipping data seeding based on active profile '{}'. Set profile to 'prod' or env var FORCE_SEED_DATA=true to enable.", activeProfile);
    }
  }

  private boolean shouldSeedData() {
    // Seed if profile is 'prod' OR if FORCE_SEED_DATA env var is explicitly 'true'
    boolean forceSeed = "true".equalsIgnoreCase(System.getenv("FORCE_SEED_DATA"));
    boolean isProdProfile = "prod".equals(activeProfile);
    logger.debug("shouldSeedData check: activeProfile='{}', isProdProfile={}, FORCE_SEED_DATA='{}', forceSeed={}",
                 activeProfile, isProdProfile, System.getenv("FORCE_SEED_DATA"), forceSeed);
    return isProdProfile || forceSeed;
  }

  private void seedAdminUser() {
    if (adminRepository.findByEmail(adminEmail).isEmpty()) {
      Admin admin = new Admin();
      admin.setName("Admin FitSync");
      admin.setEmail(adminEmail);
      admin.setPassword(passwordEncoder.encode(adminPassword));
      admin.setRole(Role.ADMIN);
      adminRepository.save(admin);
      System.out.println("Usuario administrador creado: " + adminEmail);
    }
  }

  private void seedTrainerUser() {
    if (trainerRepository.findByEmail(trainerEmail).isEmpty()) {
      Trainer trainer = new Trainer();
      trainer.setName("Entrenador Demo");
      trainer.setEmail(trainerEmail);
      trainer.setPassword(passwordEncoder.encode(trainerPassword));
      trainer.setRole(Role.TRAINER);
      trainer.setIsActive(true);
      trainerRepository.save(trainer);
      System.out.println("Usuario entrenador creado: " + trainerEmail);
    }
  }

  private void seedRegularUser() {
    if (userRepository.findByEmail(userEmail).isEmpty()) {
      User user = new User();
      user.setName("Usuario Demo");
      user.setUserLastName("Apellido");
      user.setEmail(userEmail);
      user.setPassword(passwordEncoder.encode(userPassword));
      user.setRole(Role.USER);
      user.setUserHeight(1.75f);
      user.setUserWeight(70.0f);
      user.setIsActive(true);
      user.setRegisterDate(new Date());
      user.setUserPhone("1234567890");

      // Agregar el observador de IMC
      user.addObserver(imcTrackingObserver);

      userRepository.save(user);
      System.out.println("Usuario regular creado: " + userEmail);
    }
  }
}