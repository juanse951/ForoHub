package com.aluracursos.Foro.Hub.domain.topico;

import jakarta.validation.constraints.NotBlank;

//título, mensaje, autor y curso
//Dto representa la informacion que llega
public record DatosRegistroTopico(

        @NotBlank(message = "{titulo.obligatorio}")
        String titulo,

        @NotBlank(message = "{mensaje.obligatorio}")
        String mensaje,

        @NotBlank(message = "{autor.obligatorio}")
        String autor, // Nombre del autor como string

        @NotBlank(message = "{curso.obligatorio}")
        String curso
) {
}
