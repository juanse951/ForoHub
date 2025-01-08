package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosRespuestaUsuario(

        @NotBlank(message = "{nombre.obligatorio}")
        String nombre
) {
}
