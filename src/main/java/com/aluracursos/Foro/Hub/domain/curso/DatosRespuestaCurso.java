package com.aluracursos.Foro.Hub.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record DatosRespuestaCurso(

        Long id,

        String nombre,

        Categoria categoria
) {
}
