package com.aluracursos.Foro.Hub.domain.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistroRespuesta(

        @NotBlank(message = "{mensaje.obligatorio}")
        @Schema(example = "__")
        String mensaje

) { }
