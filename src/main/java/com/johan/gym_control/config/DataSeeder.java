package com.johan.gym_control.config;

import java.util.Date;

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
    // Solo ejecutar el seed en producción o si explícitamente se configura
    if (shouldSeedData()) {
      seedAdminUser();
      seedTrainerUser();
      seedRegularUser();
    }
  }

  private boolean shouldSeedData() {
    return "prod".equals(activeProfile) ||
        "heroku".equals(activeProfile) ||
        "true".equals(System.getenv("FORCE_SEED_DATA"));
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