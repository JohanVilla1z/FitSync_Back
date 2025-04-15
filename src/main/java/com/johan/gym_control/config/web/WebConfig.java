package com.johan.gym_control.config.web;

import java.util.Arrays;

import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

  // Añadir logger
  private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

  @Value("${spring.mvc.cors.allowed-origins:http://localhost:5173}")
  private String allowedOrigins;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins.split(","))
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true)
            .maxAge(3600); // 1 hora de caché para las solicitudes pre-flight
      }
    };
  }

  @Bean
  public CorsFilter corsFilter() {
    // Log the raw string value directly
    logger.info("Configuring CorsFilter. Allowed origins from environment/property: {}",
        allowedOrigins);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    // Convertir la cadena de orígenes permitidos a una lista
    config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList("*"));
    config.setExposedHeaders(Arrays.asList("Authorization"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}