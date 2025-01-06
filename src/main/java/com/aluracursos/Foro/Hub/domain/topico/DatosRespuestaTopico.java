package com.aluracursos.Foro.Hub.domain.topico;

import java.time.LocalDateTime;
import java.util.List;

public record DatosRespuestaTopico(

        Long id,

        String titulo,

        String mensaje,

        LocalDateTime fechaCreacion,

        String status,

        String autor,

        String curso,

        List<String> respuestas
) {
}
