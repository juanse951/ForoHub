package com.aluracursos.Foro.Hub.domain.topico;


import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//título, mensaje, autor y curso
//Dto representa la informacion que llega
public record DatosRegistroTopico(

        @NotBlank(message = "{titulo.obligatorio}")
        String titulo,

        @NotBlank(message = "{mensaje.obligatorio}")
        String mensaje,

        @NotNull(message = "{autor.obligatorio}")
        Usuario autor,

        @NotNull(message = "{curso.obligatorio}")
        Curso curso
) {
}
