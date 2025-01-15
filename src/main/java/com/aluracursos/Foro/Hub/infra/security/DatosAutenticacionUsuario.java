package com.aluracursos.Foro.Hub.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;

public record DatosAutenticacionUsuario(

        @Schema(example = "__")
        String correoElectronico,

        @Schema(example = "__")
        String contrasena
) {
}
