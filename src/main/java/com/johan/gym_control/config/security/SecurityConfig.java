package com.johan.gym_control.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.johan.gym_control.models.enums.Role;
import com.johan.gym_control.services.auth.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final CustomUserDetailsService userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Activar la protección CSRF (Cross-Site Request Forgery)
        .cors(withDefaults())
        // Deshabilitar CSRF ya que usamos JWT
        .csrf(csrf -> csrf.disable())

        // Configuración de CORS usando el filtro definido en WebConfig
        // .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

        // Configuración de encabezados de seguridad (versión actualizada)
        .headers(headers -> headers
            .contentSecurityPolicy(csp -> csp
                .policyDirectives(
                    "default-src 'self'; script-src 'self'; img-src 'self' data:; style-src 'self' 'unsafe-inline';")))

        // Sin estado (stateless) - no usar sesiones
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Configuración de autorizaciones
        .authorizeHttpRequests(auth -> auth
            // Endpoints públicos
            .requestMatchers("/api/auth/**").permitAll()

            // Swagger UI y API Docs
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/api-docs/**",
                "/webjars/**")
            .permitAll()

            // Endpoints protegidos por roles
            .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
            .requestMatchers("/api/equipment/**").hasAnyRole(Role.ADMIN.name(), Role.TRAINER.name())
            .requestMatchers("/api/trainer/**").hasAnyRole(Role.ADMIN.name(), Role.TRAINER.name())
            .requestMatchers("/api/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.TRAINER.name())

            // Endpoints que requieren autenticación
            .requestMatchers("/api/profile").authenticated()
            .requestMatchers("/api/loans/**").authenticated()

            // Todo lo demás requiere autenticación
            .anyRequest().authenticated())

        // Proveedor de autenticación y filtro JWT
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}