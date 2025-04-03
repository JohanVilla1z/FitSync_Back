# FitSync

<img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-3.2.3-brightgreen.svg">
<img alt="Java" src="https://img.shields.io/badge/Java-17-orange.svg">
<img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg">

FitSync es un sistema de gestión de gimnasio que proporciona una API RESTful para el control de usuarios, registro de entradas, préstamos de equipamiento y administración general de un centro deportivo.

## Tabla de Contenidos
- [Visión General](#visión-general)
- [Características](#características)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Documentación API](#documentación-api)
- [Seguridad](#seguridad)
- [Despliegue](#despliegue)
- [Contribución](#contribución)
## Visión General
FitSync permite gestionar eficientemente un gimnasio mediante un sistema de APIs que facilitan:

- Gestión de usuarios y perfiles
- Control de acceso al gimnasio
- Préstamo y devolución de equipamiento deportivo
- Asignación de roles (administrador, entrenador, usuario)
- Informes y estadísticas de uso

El sistema está diseñado con una arquitectura basada en microservicios usando Spring Boot, siguiendo principios SOLID y un enfoque orientado a la API RESTful.
## Características

### Autenticación y Autorización
- Sistema seguro basado en JWT
- Roles diferenciados (ADMIN, TRAINER, USER)
- Endpoints protegidos según permisos

### Gestión de Usuarios
- Registro de nuevos usuarios
- Actualización de perfiles
- Gestión de información personal y biométrica

### Control de Acceso
- Registro de entradas al gimnasio
- Historial de asistencia
- Verificación de usuarios activos

### Gestión de Equipamiento
- Control de inventario de equipos
- Sistema de préstamos y devoluciones
- Seguimiento de equipamiento en uso
## Arquitectura
El proyecto sigue una arquitectura de capas:

### Patrón de Diseño
- Arquitectura basada en servicios
- Patrón Command para operaciones específicas
- Mappers para conversión entre entidades y DTOs
- Inyección de dependencias
## Requisitos
- JDK 17 o superior
- MySQL 8.0 o superior
- Gradle 8.0 o superior
- IDE compatible con Spring Boot (recomendado: IntelliJ IDEA, VS Code)
## Instalación
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tuUsuario/FitSync.git
   cd FitSync
   ```

## Configuración

### Base de datos
Configura la conexión a tu base de datos MySQL en application.properties:
   ./gradlew bootRun
   ```
IDE compatible con Spring Boot (recomendado: IntelliJ IDEA, VS Code)
Instalación
## Documentación API
La documentación completa de la API está disponible a través de Swagger UI:

- URL Local: http://localhost:8080/swagger-ui.html
- Especificación OpenAPI: http://localhost:8080/v3/api-docs

### Endpoints Principales
- Autenticación
- Registros de Entrada
## Seguridad

### Autenticación con JWT
- Obtén un token mediante una solicitud a `/api/auth/login`
- Incluye el token en el encabezado de las solicitudes:
  ```
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  ```


## Despliegue

### Construir JAR Ejecutable
Seguridad
Autenticación con JWT
Obtén un token mediante una solicitud a /api/auth/login
Incluye el token en el encabezado de las solicitudes:
Control de Acceso Basado en Roles
## Contribución
1. Fork el repositorio
2. Crea una rama para tu característica (`git checkout -b feature/amazing-feature`)
3. Realiza los cambios necesarios y commitea (`git commit -m 'Add amazing feature'`)
4. Push a tu rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

### Estándares de Código
- Sigue el estilo de código de Google para Java
- Incluye pruebas unitarias para nuevas características
```markdown
## Licencia
Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.
Habilita HTTPS para comunicaciones seguras
Implementa monitoreo y logging
Contribución
Fork el repositorio
Crea una rama para tu característica (git checkout -b feature/amazing-feature)
Realiza los cambios necesarios y commitea (git commit -m 'Add amazing feature')
Push a tu rama (git push origin feature/amazing-feature)
Abre un Pull Request
Estándares de Código
Sigue el estilo de código de Google para Java
Incluye pruebas unitarias para nuevas características
Documenta los nuevos endpoints con anotaciones de Swagger
Licencia
Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.

Desarrollado por el Equipo FitSync - javilla22@soy.sena.edu.co