package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.Pattern;

public record DatosActualizarUsuario(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        String nombre,

        String correoElectronico,

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "{password.invalido}")
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
