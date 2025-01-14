package com.aluracursos.Foro.Hub.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record DatosActualizarUsuario(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        @Schema(example = "{update.message}")
        String nombre,

        @Schema(example = "{update.message}")
        String correoElectronico,

        @Schema(example = "{update.message}")
        String contrasena

        ) {

        public DatosActualizarUsuario(Usuario usuario){
              this(
               usuario.getNombre(),
               usuario.getCorreoElectronico(),
               usuario.getContrasena()
                );
        }
}
