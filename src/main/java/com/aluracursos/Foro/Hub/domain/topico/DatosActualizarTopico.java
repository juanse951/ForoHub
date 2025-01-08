package com.aluracursos.Foro.Hub.domain.topico;

import com.aluracursos.Foro.Hub.domain.curso.DatosActualizarCurso;
import com.aluracursos.Foro.Hub.domain.usuario.DatosActualizarUsuario;

public record DatosActualizarTopico(

        Long id,

        String titulo,

        String mensaje,

        DatosActualizarUsuario autor,

        DatosActualizarCurso curso
) {
    public DatosActualizarTopico(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                new DatosActualizarUsuario(topico.getAutor().getId(), topico.getAutor().getNombre()),
                new DatosActualizarCurso(topico.getCurso().getId(), topico.getCurso().getNombre()));
    }
}