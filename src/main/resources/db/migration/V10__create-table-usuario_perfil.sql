-- Eliminar la clave foránea de perfil_id
ALTER TABLE usuarios DROP FOREIGN KEY usuarios_ibfk_1;

-- Crear la tabla usuario_perfil
CREATE TABLE usuario_perfil (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (perfil_id) REFERENCES perfiles(id) ON DELETE CASCADE
);

-- Insertar los datos correspondientes de usuarios en usuario_perfil
INSERT INTO usuario_perfil (usuario_id, perfil_id)
SELECT id AS usuario_id, perfil_id
FROM usuarios
WHERE perfil_id IS NOT NULL;

-- Finalmente, eliminar la columna perfil_id de la tabla usuarios
ALTER TABLE usuarios DROP COLUMN perfil_id;
