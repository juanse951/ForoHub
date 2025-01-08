package com.aluracursos.Foro.Hub.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record DatosRespuestaCurso(

        @NotBlank(message = "{nombre.obligatorio}")
        String nombre
) {
}
