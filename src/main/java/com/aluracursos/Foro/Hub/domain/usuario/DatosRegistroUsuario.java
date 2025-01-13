package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroUsuario(


        @NotBlank(message = "{nombre.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        String nombre,

        @NotBlank(message = "{email.obligatorio}")
        String correoElectronico,

        @NotBlank(message = "{password.obligatorio}")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "{password.invalido}")
        String contrasena,

        TipoPerfil perfil

) { }