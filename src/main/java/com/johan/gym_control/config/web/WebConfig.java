package com.johan.gym_control.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins(
            "https://fitsyncapp.netlify.app",
            "http://localhost:5173",
            "https://worthy-warmth-production.up.railway.app")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }
}