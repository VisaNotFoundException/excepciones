BEGIN;

-- =========================================================
-- V2: Normalización de roles
-- - Crea tabla roles_de_usuario (id, nombre)
-- - Agrega usuarios.rol_id
-- - Migra usuarios.rol (TEXT) -> usuarios.rol_id
-- - Crea FK y NOT NULL
-- - Elimina la columna vieja usuarios.rol
-- =========================================================

-- 1) Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles_de_usuario (
  id     BIGSERIAL PRIMARY KEY,
  nombre TEXT NOT NULL UNIQUE
);

-- 2) Insertar roles base (alineados con el enum Roles)
INSERT INTO roles_de_usuario(nombre)
VALUES
  ('USUARIO'),
  ('ADMIN'),
  ('VIP'),
  ('OPERADOR'),
  ('BANEADO')
ON CONFLICT (nombre) DO NOTHING;

-- 3) Agregar columna rol_id a usuarios
ALTER TABLE usuarios
ADD COLUMN IF NOT EXISTS rol_id BIGINT;

-- 4) Migrar datos viejos (rol TEXT → rol_id)
-- Si había valores raros, quedarán NULL y se arreglan en el paso 4b.
UPDATE usuarios u
SET rol_id = r.id
FROM roles_de_usuario r
WHERE u.rol = r.nombre;

-- 4b) Default defensivo: si algún usuario quedó sin rol,
-- lo mandamos a USUARIO antes de poner NOT NULL.
UPDATE usuarios
SET rol_id = (SELECT id FROM roles_de_usuario WHERE nombre = 'USUARIO')
WHERE rol_id IS NULL;

-- 5) Crear FK si no existe
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.table_constraints
    WHERE constraint_name = 'fk_usuario_rol'
      AND table_name = 'usuarios'
  ) THEN
    ALTER TABLE usuarios
      ADD CONSTRAINT fk_usuario_rol
      FOREIGN KEY (rol_id)
      REFERENCES roles_de_usuario(id);
  END IF;
END $$;

-- 6) Setear NOT NULL
ALTER TABLE usuarios
ALTER COLUMN rol_id SET NOT NULL;

-- 7) Borrar columna vieja
ALTER TABLE usuarios
DROP COLUMN IF EXISTS rol;

COMMIT;
