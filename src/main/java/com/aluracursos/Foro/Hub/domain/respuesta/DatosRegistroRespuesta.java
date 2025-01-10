package com.aluracursos.Foro.Hub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroRespuesta(

        @NotBlank(message = "{mensaje.obligatorio}")
        String mensaje

) { }
