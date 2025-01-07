package com.aluracursos.Foro.Hub.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public record DatosRegistroCurso(

      @NotBlank(message = "{nombre.obligatorio}")
      String nombre,

      @NotNull(message = "{categoria.obligatorio}")
      Categoria categoria
) {
    public static Curso registro(String nombre){
        Categoria categoria = Categoria.GENERAL;
        return new Curso(null, nombre, categoria,new ArrayList<>());
    }
}
