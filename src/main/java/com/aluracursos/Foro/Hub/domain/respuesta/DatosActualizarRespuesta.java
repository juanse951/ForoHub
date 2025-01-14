package com.aluracursos.Foro.Hub.domain.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;

public record DatosActualizarRespuesta(

        @Schema(example = "Déjalo en blanco para mantener o actualiza")
        String mensaje,

        @Schema(example = "Déjalo en blanco para mantener PENDIENTE o escoge SOLUCIONADO o NO SOLUCIONADO")
        RespuestaStatus solucion
) {
        public DatosActualizarRespuesta(Respuesta respuesta){
                this(
                        respuesta.getMensaje(),
                        respuesta.getSolucion()
                        );
        }
}
