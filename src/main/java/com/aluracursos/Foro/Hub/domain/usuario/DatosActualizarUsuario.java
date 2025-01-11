package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.Pattern;

public record DatosActualizarUsuario(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        String nombre

        //        @NotBlank(message = "{email.obligatorio}")
//        @Email(message = "{email.invalido}")
//        String correoElectronico,
//
//        @NotBlank(message = "{password.obligatorio}")
//        @Pattern(regexp = "^(defaultPassword|(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,})$",
//                message = "{password.invalido}")
//        String contrasena

        ) { }
