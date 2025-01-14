CREATE TABLE respuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(500) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    solucion VARCHAR(255) NOT NULL,
    topico_id BIGINT,
    autor_id BIGINT,
    FOREIGN KEY (topico_id) REFERENCES topicos(id),
    FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);

