-- Script de inicialización de la base de datos
-- Este script se ejecutará automáticamente cuando el contenedor de MySQL inicie por primera vez

-- Asegurar que estamos usando la base de datos correcta
USE gym_control;

-- Esto es opcional ya que Spring Boot puede crear las tablas con hibernate.ddl-auto=update
-- Sin embargo, podemos agregar índices adicionales o configuraciones específicas aquí

-- Crear índices para mejorar el rendimiento si es necesario
-- Por ejemplo: 
-- ALTER TABLE user ADD INDEX idx_email (email);

-- Insertar datos iniciales solo si la tabla está vacía (para evitar duplicados en reinicios)
-- Por ejemplo, insertar un usuario admin inicial si no existe ninguno:

-- Verificamos si hay algún usuario admin ya creado
-- Esta verificación se hace aquí como respaldo, pero Spring Boot probablemente ya maneja esto con @PostConstruct
DELIMITER //
CREATE PROCEDURE create_initial_admin_if_needed()
BEGIN
    DECLARE admin_count INT;
    
    -- Verificar si la tabla 'user' existe para evitar errores durante la primera ejecución
    SELECT COUNT(*) INTO @table_exists 
    FROM information_schema.tables 
    WHERE table_schema = 'gym_control' AND table_name = 'user';
    
    IF @table_exists > 0 THEN
        -- Verificar si hay algún admin
        SELECT COUNT(*) INTO admin_count FROM `user` WHERE role = 'ADMIN' LIMIT 1;
        
        -- Si no hay admin, crear uno por defecto (Spring Boot también probablemente hará esto)
        IF admin_count = 0 THEN
            -- Nota: Esto es una suplantación, Spring debería gestionar esto con BCrypt
            -- El password real debería estar hasheado
            INSERT INTO `user` (email, password, first_name, last_name, role, enabled, created_at, updated_at) 
            VALUES ('admin@fitsync.com', 'PLACEHOLDER_WILL_BE_REPLACED_BY_SPRING', 'Admin', 'User', 'ADMIN', 1, NOW(), NOW());
        END IF;
    END IF;
END//
DELIMITER ;

-- Ejecutar el procedimiento
CALL create_initial_admin_if_needed();

-- Eliminar el procedimiento para mantener limpia la base de datos
DROP PROCEDURE IF EXISTS create_initial_admin_if_needed;

-- Aplicar cualquier otra configuración específica de MySQL que necesites
SET GLOBAL time_zone = '+00:00';
SET GLOBAL max_connections = 1000;
SET GLOBAL innodb_buffer_pool_size = 268435456; -- 256MB, ajustar según las necesidades