package com.aluracursos.Foro.Hub.domain.topico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroTopico(

        @NotBlank(message = "{titulo.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{titulo.error}")
        @Schema(example = "_😊_")
        String titulo,

        @NotBlank(message = "{mensaje.obligatorio}")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{mensaje.error}")
        @Schema(example = "_😊_")
        String mensaje,

        @NotNull(message = "{autor.obligatorio}")
        Long autor_id,

        @NotNull(message = "{curso.obligatorio}")
        Long curso_id
) {
}
