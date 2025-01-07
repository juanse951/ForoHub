package com.aluracursos.Foro.Hub.domain.topico;

import com.aluracursos.Foro.Hub.domain.curso.DatosRespuestaCurso;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRespuestaUsuario;

import java.time.LocalDateTime;

public record DatosListadoTopico(

        Long id,

        String titulo,

        String mensaje,

        LocalDateTime fechaCreacion,

        String status,

        DatosRespuestaUsuario autor,

        DatosRespuestaCurso curso
) {
    public DatosListadoTopico(Topico topico){
        this(
        topico.getId(),
        topico.getTitulo(),
        topico.getMensaje(),
        topico.getFechaCreacion(),
        topico.getStatus().toString(),
                new DatosRespuestaUsuario(
                        topico.getAutor().getNombre()
                ),
                new DatosRespuestaCurso(
                        topico.getCurso().getNombre()
                ));
    }
}
