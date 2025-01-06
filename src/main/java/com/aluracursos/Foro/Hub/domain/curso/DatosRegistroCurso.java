package com.aluracursos.Foro.Hub.domain.curso;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public record DatosRegistroCurso(

      @NotBlank(message = "{nombre.obligatorio}")
      String nombre,

      @NotBlank(message = "{categoria.obligatorio}")
      String categoria
) {
    public static Curso registro(String nombre){
        Categoria categoria = Categoria.GENERAL;
        return new Curso(null, nombre, categoria,new ArrayList<>());
    }
}
