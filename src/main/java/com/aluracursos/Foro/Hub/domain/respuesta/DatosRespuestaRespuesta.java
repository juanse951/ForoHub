package com.aluracursos.Foro.Hub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosRespuestaRespuesta(

        Long id,

        String mensaje,

        String topico,

        LocalDateTime fechaCreacion,

        String autor,

        String solucion
){
    public DatosRespuestaRespuesta(Respuesta respuesta){
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getTopico().getTitulo(),
                respuesta.getFechaCreacion(),
                respuesta.getAutor().getNombre(),
                respuesta.getSolucion().toString()
        );
    }
}
