package com.aluracursos.Foro.Hub.domain.curso;


import jakarta.validation.constraints.Pattern;

public record DatosActualizarCurso(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{curso.error}")
        String nombre
) {
}
