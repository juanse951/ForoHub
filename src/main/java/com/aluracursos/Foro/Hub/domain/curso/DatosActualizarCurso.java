package com.aluracursos.Foro.Hub.domain.curso;


import jakarta.validation.constraints.Pattern;

public record DatosActualizarCurso(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{curso.error}")
        String nombre
) {
}
