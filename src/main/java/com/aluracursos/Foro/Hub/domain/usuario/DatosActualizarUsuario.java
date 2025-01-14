package com.aluracursos.Foro.Hub.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record DatosActualizarUsuario(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        @Schema(example = "Déjalo en blanco para mantener o actualiza")
        String nombre,

        @Schema(example = "Déjalo en blanco para mantener o actualiza")
        String correoElectronico,

        @Schema(example = "Déjalo en blanco para mantener o actualiza")
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
