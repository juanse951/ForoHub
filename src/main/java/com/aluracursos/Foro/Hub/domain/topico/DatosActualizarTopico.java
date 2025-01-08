package com.aluracursos.Foro.Hub.domain.topico;

import com.aluracursos.Foro.Hub.domain.curso.DatosActualizarCurso;
import com.aluracursos.Foro.Hub.domain.usuario.DatosActualizarUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public record DatosActualizarTopico(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$", message = "{titulo.error}")
        String titulo,

        String mensaje,

        @Valid
        DatosActualizarUsuario autor,

        @Valid
        DatosActualizarCurso curso
) {
    public DatosActualizarTopico(Topico topico){
        this(
                topico.getTitulo(),
                topico.getMensaje(),
                new DatosActualizarUsuario(topico.getAutor().getNombre()),
                new DatosActualizarCurso(topico.getCurso().getNombre()));
    }
}