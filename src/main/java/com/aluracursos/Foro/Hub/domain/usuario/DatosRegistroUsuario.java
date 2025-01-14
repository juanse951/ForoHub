package com.aluracursos.Foro.Hub.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroUsuario(


        @NotBlank(message = "{nombre.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{autor.error}")
        @Schema(example = "__")
        String nombre,

        @NotBlank(message = "{email.obligatorio}")
        @Schema(example = "__")
        String correoElectronico,

        @NotBlank(message = "{password.obligatorio}")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "{password.invalido}")
        @Schema(example = "__")
        String contrasena
) { }