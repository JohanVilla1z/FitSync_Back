package com.johan.gym_control.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para endpoint de health check utilizado por Docker y sistemas de
 * orquestación
 * para verificar el estado de la aplicación.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

  @GetMapping
  public ResponseEntity<Map<String, Object>> healthCheck() {
    Map<String, Object> status = new HashMap<>();
    status.put("status", "UP");
    status.put("timestamp", System.currentTimeMillis());

    // Aquí podrías agregar más información sobre componentes específicos
    // como la base de datos, servicios externos, etc.

    return ResponseEntity.ok(status);
  }
}