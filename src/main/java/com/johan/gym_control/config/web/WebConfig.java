package com.johan.gym_control.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Optional;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    // Lee los orígenes permitidos desde la variable de entorno
    String allowedOriginsEnv = Optional.ofNullable(System.getenv("ALLOWED_ORIGINS"))
        .orElse("https://fitsyncapp.netlify.app,http://localhost:5173");
    String[] allowedOrigins = allowedOriginsEnv.split("\\s*,\\s*");

    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true); // Solo permitido si no usas '*'
  }

  // CorsFilter eliminado para desactivar protección CORS personalizada

}