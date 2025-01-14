package com.aluracursos.Foro.Hub.domain.curso;

import jakarta.validation.constraints.Pattern;

public record DatosActualizarCurso(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{curso.error}")
        String nombre,

        Categoria categoria
) {
        public DatosActualizarCurso(Curso curso){
                this(
                        curso.getNombre(),
                        curso.getCategoria()
                );
        }
}
