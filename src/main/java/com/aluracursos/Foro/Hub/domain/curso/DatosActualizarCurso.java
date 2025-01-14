package com.aluracursos.Foro.Hub.domain.curso;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record DatosActualizarCurso(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{curso.error}")
        String nombre,

        @Schema(example = "Déjalo en blanco para mantener o elige una categoría de /categorias")
        Categoria categoria
) {
        public DatosActualizarCurso(Curso curso){
                this(
                        curso.getNombre(),
                        curso.getCategoria()
                );
        }
}
