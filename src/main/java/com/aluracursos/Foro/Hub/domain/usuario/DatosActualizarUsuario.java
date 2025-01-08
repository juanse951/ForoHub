package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.Pattern;

public record DatosActualizarUsuario(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        String nombre
        ) {
}
