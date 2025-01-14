package com.aluracursos.Foro.Hub.domain.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(

        @NotBlank(message = "{mensaje.obligatorio}")
        @Schema(example = "__")
        String mensaje,

        @NotNull(message = "{autor.obligatorio}")
        Long autor_id
) { }
