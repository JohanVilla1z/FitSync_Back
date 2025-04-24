# FitSync

<img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-3.2.3-brightgreen.svg">
<img alt="Java" src="https://img.shields.io/badge/Java-17-orange.svg">
<img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg">

FitSync es un sistema de gestión de gimnasio que proporciona una API RESTful para el control de usuarios, registro de entradas, préstamos de equipamiento y administración general de un centro deportivo.

## Tabla de Contenidos

- [Visión General](#visión-general)
- [Características](#características)
- [Arquitectura](#arquitectura)
- [Diagrama de Arquitectura](#diagrama-de-arquitectura)
- [Estructura de Carpetas](#estructura-de-carpetas)
- [Ejemplo de Flujo de Uso](#ejemplo-de-flujo-de-uso)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Documentación API](#documentación-api)
- [Seguridad](#seguridad)
- [Despliegue](#despliegue)
- [Contribución](#contribución)
- [Licencia](#licencia)

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

- **Capa de Presentación (Controllers):** Maneja las solicitudes HTTP, valida la entrada y delega a la capa de servicio. Utiliza DTOs (Data Transfer Objects) para la comunicación.
- **Capa de Servicio (Services):** Contiene la lógica de negocio principal. Orquesta las operaciones interactuando con los repositorios.
- **Capa de Datos (Repositories):** Interactúa con la base de datos utilizando Spring Data JPA. Define interfaces para las operaciones CRUD.
- **Dominio (Entities):** Representa las entidades del negocio (Usuario, Equipo, RegistroEntrada, etc.).

### Patrón de Diseño

- **Arquitectura basada en servicios:** Componentes modulares y reutilizables.
- **Patrón Command:** Utilizado para encapsular operaciones específicas como acciones (ej., `RegistrarUsuarioCommand`). Esto ayuda a desacoplar el invocador del receptor de la acción.
  - _Ejemplo:_ Un comando podría encapsular toda la información necesaria para registrar un nuevo usuario (datos del usuario, rol, etc.) y el método `execute()` contendría la lógica para llamar al servicio correspondiente.
- **Mappers (MapStruct):** Para la conversión eficiente y sin boilerplate entre Entidades JPA y DTOs. Asegura que las capas no dependan directamente de las representaciones de datos de otras capas.
  - _Ejemplo:_ Un `UsuarioMapper` convertiría un `Usuario` (entidad) a un `UsuarioDTO` (para la API) y viceversa.
- **Inyección de Dependencias (Spring DI):** Spring gestiona la creación e inyección de dependencias (servicios, repositorios, etc.), promoviendo el bajo acoplamiento y la alta cohesión.
  - _Ejemplo:_ `@Autowired` o inyección por constructor para proporcionar instancias de `UsuarioRepository` a `UsuarioService`.

## Diagrama de Arquitectura

```mermaid
graph TD
    A[Cliente API] --> B(Controller);
    B --> C{Service};
    C --> D[Repository];
    D --> E[(Database)];
    B --> F[DTOs];
    C --> F;
    C --> G[Entities];
    D --> G;
    H[Security Filter] --> B;
    I[Mappers] --> B;
    I --> C;

    subgraph "Capa Presentación"
        B
        F
    end
    subgraph "Capa Servicio"
        C
    end
    subgraph "Capa Datos"
        D
        G
        E
    end
    subgraph "Utilidades"
        I
    end
    subgraph "Seguridad"
        H
    end

```

_(Diagrama simplificado mostrando el flujo principal y componentes)_

## Estructura de Carpetas

```
fitsync/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/fitsync/fitsync/
│   │   │       ├── config/       # Configuraciones (Seguridad, Swagger, Beans)
│   │   │       ├── controllers/  # Controladores REST API
│   │   │       ├── dto/          # Data Transfer Objects (Request/Response)
│   │   │       ├── entities/     # Entidades JPA (Modelo de Dominio)
│   │   │       ├── enums/        # Enumeraciones (Roles, Estados)
│   │   │       ├── exceptions/   # Clases de excepciones personalizadas
│   │   │       ├── mappers/      # Interfaces MapStruct para mapeo DTO/Entidad
│   │   │       ├── repositories/ # Interfaces Spring Data JPA
│   │   │       ├── security/     # Clases relacionadas con JWT y seguridad
│   │   │       ├── services/     # Lógica de negocio (Interfaces e Implementaciones)
│   │   │       └── FitSyncApplication.java # Clase principal de Spring Boot
│   │   └── resources/
│   │       ├── application.properties # Configuración de la aplicación
│   │       └── db/migration/        # Scripts de migración Flyway (si se usa)
│   └── test/                     # Pruebas unitarias y de integración
├── .gradle/
├── build/                      # Archivos generados por la compilación
├── gradle/
├── gradlew
├── gradlew.bat
├── build.gradle                # Script de construcción Gradle
└── settings.gradle
```

## Ejemplo de Flujo de Uso

1.  **Registro de Usuario:**
    - Un nuevo usuario envía una petición POST a `/api/users/register` con sus datos (nombre, email, contraseña).
    - El `UserController` recibe la petición y valida el `UserRegistrationDTO`.
    - Llama a `UserService.registerUser(dto)`.
    - El `UserService` valida la lógica de negocio (ej., email no existente), hashea la contraseña, crea una entidad `Usuario` y la guarda usando `UserRepository`.
    - Devuelve un `UserDTO` con los datos del usuario creado (sin contraseña).
2.  **Login:**
    - El usuario envía una petición POST a `/api/auth/login` con email y contraseña.
    - El `AuthController` recibe las credenciales.
    - Llama a `AuthenticationService.authenticate(credentials)`.
    - El servicio verifica las credenciales contra la base de datos.
    - Si son válidas, genera un token JWT usando `JwtService`.
    - Devuelve el token JWT al usuario.
3.  **Préstamo de Equipo (Usuario Autenticado):**
    - El usuario envía una petición POST a `/api/loans` incluyendo el ID del equipo y su token JWT en el header `Authorization`.
    - El filtro de seguridad (`JwtAuthenticationFilter`) valida el token y establece el contexto de seguridad.
    - El `LoanController` recibe la petición.
    - Llama a `LoanService.createLoan(userId, equipmentId)`.
    - El `LoanService` verifica la disponibilidad del equipo, crea un registro de préstamo y actualiza el estado del equipo.
    - Devuelve los detalles del préstamo.

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

Configura la conexión a tu base de datos MySQL en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitsync_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=tu_usuario_db
spring.datasource.password=tu_contraseña_db
spring.jpa.hibernate.ddl-auto=update # o validate en producción
spring.jpa.show-sql=true

# Configuración JWT (ejemplo)
jwt.secret=TU_SECRETO_JWT_MUY_SEGURO_AQUI_DEBE_SER_LARGO
jwt.expiration=86400000 # 1 día en milisegundos
```

### Ejecutar la aplicación

```bash
./gradlew bootRun
```

## Documentación API

La documentación completa de la API está disponible a través de Swagger UI una vez que la aplicación está en ejecución:

- URL Local: `http://localhost:8080/swagger-ui.html`
- Especificación OpenAPI: `http://localhost:8080/v3/api-docs`

### Endpoints Principales

- `/api/auth`: Autenticación (login).
- `/api/users`: Gestión de usuarios (registro, perfiles).
- `/api/entries`: Registro de entradas al gimnasio.
- `/api/equipment`: Gestión de equipamiento.
- `/api/loans`: Gestión de préstamos de equipamiento.

### Ejemplo de Peticiones y Respuestas

**1. Registro de Usuario (POST /api/users/register)**

- **Request Body:**
  ```json
  {
    "name": "Juan Perez",
    "email": "juan.perez@email.com",
    "password": "password123",
    "role": "USER"
  }
  ```
- **Response Body (201 Created):**
  ```json
  {
    "id": "uuid-generado-1",
    "name": "Juan Perez",
    "email": "juan.perez@email.com",
    "role": "USER",
    "active": true
  }
  ```

**2. Login (POST /api/auth/login)**

- **Request Body:**
  ```json
  {
    "email": "juan.perez@email.com",
    "password": "password123"
  }
  ```
- **Response Body (200 OK):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

**3. Crear Préstamo (POST /api/loans - Requiere Auth)**

- **Request Body:**
  ```json
  {
    "equipmentId": "uuid-equipo-123"
    // userId se obtiene del token JWT
  }
  ```
- **Response Body (201 Created):**
  ```json
  {
    "id": "uuid-prestamo-456",
    "userId": "uuid-usuario-1",
    "equipmentId": "uuid-equipo-123",
    "loanDate": "2024-08-15T10:00:00Z",
    "returnDate": null,
    "status": "ACTIVE"
  }
  ```

## Seguridad

### Autenticación con JWT

- Obtén un token mediante una solicitud POST a `/api/auth/login` con tus credenciales.
- Incluye el token JWT recibido en el encabezado `Authorization` de las solicitudes subsiguientes a endpoints protegidos:
  ```
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  ```
- El sistema utiliza `JwtAuthenticationFilter` para validar el token en cada petición protegida y establecer el contexto de seguridad de Spring.

### Control de Acceso Basado en Roles

- Los endpoints están protegidos usando anotaciones como `@PreAuthorize("hasRole('ADMIN')")` o `@PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")`.
- Spring Security evalúa los roles del usuario autenticado (extraídos del token JWT) contra los permisos requeridos por el endpoint.

### Recomendaciones Adicionales

- Utiliza HTTPS en producción.
- Gestiona los secretos (clave JWT, contraseñas de DB) de forma segura (variables de entorno, gestores de secretos).
- Implementa logging y monitoreo adecuados.

## Despliegue

### Construir JAR Ejecutable

```bash
./gradlew bootJar
```

Esto generará un archivo JAR ejecutable en `build/libs/`.

### Despliegue en Producción (Ejemplo con Railway)

1.  **Configuración del Proyecto en Railway:**
    - Conecta tu repositorio Git a Railway.
    - Railway detectará que es un proyecto Java/Spring Boot.
    - Configura el comando de build (usualmente `./gradlew bootJar`) y el comando de start (`java -jar build/libs/FitSync-0.0.1-SNAPSHOT.jar` - ajusta el nombre del JAR).
2.  **Variables de Entorno Críticas:**
    - Configura las siguientes variables de entorno en el panel de Railway:
      - `SPRING_DATASOURCE_URL`: La URL JDBC de tu base de datos en producción (Railway puede proveer una base de datos o puedes conectar una externa).
      - `SPRING_DATASOURCE_USERNAME`: Usuario de la base de datos.
      - `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos.
      - `JWT_SECRET`: Tu clave secreta JWT (debe ser fuerte y diferente a la de desarrollo).
      - `JWT_EXPIRATION`: Tiempo de expiración del token.
      - `SPRING_JPA_HIBERNATE_DDL_AUTO`: Configúralo como `validate` o `none` en producción. Usa Flyway o Liquibase para gestionar migraciones de esquema.
      - `SERVER_PORT`: Railway asigna un puerto, pero puedes definirlo si es necesario (usualmente no se requiere).
3.  **Base de Datos:**
    - Asegúrate de que la base de datos esté configurada y accesible desde la aplicación desplegada.
    - Aplica las migraciones de base de datos necesarias antes o durante el despliegue.
4.  **Escalado y Monitoreo:**
    - Railway permite escalar los recursos (CPU, RAM) de tu servicio fácilmente desde su dashboard.
    - Utiliza las herramientas de logging y métricas de Railway para monitorear el rendimiento y los errores de la aplicación.
    - Considera integrar herramientas de monitoreo externas (ej., Datadog, New Relic) si necesitas análisis más profundos.
5.  **HTTPS:**
    - Railway generalmente proporciona certificados SSL/TLS automáticamente para los dominios personalizados o los subdominios `*.up.railway.app`.

## Contribución

1. Fork el repositorio
2. Crea una rama para tu característica (`git checkout -b feature/amazing-feature`)
3. Realiza los cambios necesarios y commitea (`git commit -m 'Add amazing feature'`)
4. Push a tu rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

### Estándares de Código

- Sigue el estilo de código de Google para Java
- Incluye pruebas unitarias para nuevas características
- Documenta los nuevos endpoints con anotaciones de Swagger

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo `LICENSE` para más detalles.

---

Desarrollado por el Equipo FitSync - javilla22@soy.sena.edu.co
