package com.aluracursos.Foro.Hub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record DatosRegistroRespuesta(

        @NotBlank(message = "{mensaje.obligatorio}")
        String mensaje,

        @NotBlank(message = "{fecha.error}")
        LocalDateTime fechaCreacion
) {
    public static Respuesta registro(String mensaje) {
        // Asignar un valor predeterminado si el mensaje es nulo o vacío
        String mensajeFinal = (mensaje == null || mensaje.isBlank()) ? "aún sin respuesta" : mensaje;

        LocalDateTime fechaCreacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return new Respuesta(null, mensajeFinal, null, fechaCreacion, null, false);
    }
}
