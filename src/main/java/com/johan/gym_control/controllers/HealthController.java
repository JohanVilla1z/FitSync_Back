package com.johan.gym_control.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para verificar el estado de la aplicación.
 * Utilizado por herramientas de monitoreo y healthchecks de Docker.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

  @GetMapping
  public ResponseEntity<Map<String, Object>> healthCheck() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "UP");
    response.put("timestamp", new Date().toString());
    response.put("service", "FitSync API");

    // Puedes agregar más información relevante para el monitoreo
    Runtime runtime = Runtime.getRuntime();
    response.put("memory", Map.of(
        "total", runtime.totalMemory(),
        "free", runtime.freeMemory(),
        "max", runtime.maxMemory()));

    return ResponseEntity.ok(response);
  }
}