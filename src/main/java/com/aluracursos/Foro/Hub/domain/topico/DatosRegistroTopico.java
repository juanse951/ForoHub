package com.aluracursos.Foro.Hub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

//título, mensaje, autor y curso
//Dto representa la informacion que llega
public record DatosRegistroTopico(

        @NotBlank(message = "{titulo.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{titulo.error}")
        String titulo,

        @NotBlank(message = "{mensaje.obligatorio}")
        String mensaje,

        @NotBlank(message = "{autor.obligatorio}")
        String autor, // Nombre del autor como string

        @NotBlank(message = "{curso.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{curso.error}")
        String curso
) {
}
