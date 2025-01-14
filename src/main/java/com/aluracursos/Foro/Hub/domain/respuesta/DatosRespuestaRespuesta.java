package com.aluracursos.Foro.Hub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosRespuestaRespuesta(

        Long id,

        String mensaje,



        LocalDateTime fechaCreacion,



        RespuestaStatus solucion
){
    public DatosRespuestaRespuesta(Respuesta respuesta){
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getSolucion()
        );
    }
}
