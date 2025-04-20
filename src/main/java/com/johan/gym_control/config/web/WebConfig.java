package com.johan.gym_control.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(
            "https://fitsyncapp.netlify.app",
            "http://localhost:5173",
            "https://worthy-warmth-production.up.railway.app"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }

  // CorsFilter eliminado para desactivar protección CORS personalizada

}

// No necesitas cambios aquí si ya tienes lo siguiente en SecurityConfig:
// http.cors(); // en la configuración de HttpSecurity

// Si no tienes SecurityConfig, crea una clase así:

/*
 * import org.springframework.context.annotation.Bean;
 * import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.web.SecurityFilterChain;
 * 
 * @Configuration
 * public class SecurityConfig {
 * 
 * @Bean
 * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 * http
 * .cors() // Habilita CORS usando la configuración de WebMvcConfigurer
 * .and()
 * .csrf().disable()
 * // ...otras configuraciones de seguridad...
 * ;
 * return http.build();
 * }
 * }
 */