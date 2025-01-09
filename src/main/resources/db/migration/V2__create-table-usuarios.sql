CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    correo_electronico VARCHAR(255) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    perfil_id BIGINT,
    FOREIGN KEY (perfil_id) REFERENCES perfiles(id)
);

