package com.johan.gym_control.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración para activar el sistema de caché en entornos de producción
 * Mejora el rendimiento al almacenar en caché resultados de consultas
 * frecuentes
 */
@Configuration
@EnableCaching
@Profile("prod") // Solo activa en el perfil de producción
public class CacheConfig {
    // La configuración específica se maneja en application-prod.properties
}