package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarUsuario(

        @NotNull
        Long id,

        String nombre
        ) {
}
