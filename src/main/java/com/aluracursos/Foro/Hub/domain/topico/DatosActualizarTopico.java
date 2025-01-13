package com.aluracursos.Foro.Hub.domain.topico;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DatosActualizarTopico(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{titulo.error}")
        String titulo,

        String mensaje,

        @NotNull(message = "{autor.obligatorio}")
        Long autor_id,

        @NotNull(message = "{curso.obligatorio}")
        Long curso_id
) {
    public DatosActualizarTopico(Topico topico){
        this(
        topico.getTitulo(),
        topico.getMensaje(),
        topico.getAutor().getId(),
        topico.getCurso().getId());
    }
}