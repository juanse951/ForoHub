package com.aluracursos.Foro.Hub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

//título, mensaje, autor y curso
//Dto representa la informacion que llega
public record DatosRegistroTopico(

        @NotBlank(message = "{titulo.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{titulo.error}")
        String titulo,

        @NotBlank(message = "{mensaje.obligatorio}")
        String mensaje,

        @NotNull(message = "{autor.obligatorio}")
        Long autor_id,

        @NotNull(message = "{curso.obligatorio}")
        Long curso_id
) {
}
