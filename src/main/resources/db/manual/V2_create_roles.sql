BEGIN;

-- =============================
-- 1) Crear tabla de roles
-- =============================
CREATE TABLE IF NOT EXISTS roles_de_usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

-- =============================
-- 2) Insertar roles base
-- =============================
INSERT INTO roles_de_usuario(nombre)
VALUES
('USUARIO'),
('ADMIN'),
('VIP'),
('OPERADOR'),
('BANEADO')
ON CONFLICT (nombre) DO NOTHING;


-- =============================
-- 3) Agregar columna rol_id a usuarios
-- =============================
ALTER TABLE usuarios
ADD COLUMN IF NOT EXISTS rol_id BIGINT;


-- =============================
-- 4) Migrar datos viejos (rol TEXT → rol_id)
-- =============================
UPDATE usuarios u
SET rol_id = r.id
FROM roles_de_usuario r
WHERE u.rol = r.nombre;


-- =============================
-- 5) Hacer FK y NOT NULL
-- =============================
ALTER TABLE usuarios
ADD CONSTRAINT fk_usuario_rol
FOREIGN KEY (rol_id)
REFERENCES roles_de_usuario(id);

ALTER TABLE usuarios
ALTER COLUMN rol_id SET NOT NULL;


-- =============================
-- 6) Borrar columna vieja
-- =============================
ALTER TABLE usuarios
DROP COLUMN IF EXISTS rol;


COMMIT;
