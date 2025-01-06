package com.aluracursos.Foro.Hub.domain.curso;

import java.util.ArrayList;

public record DatosRespuestaCurso(

      String nombre,

      String categoria
) {
    public static Curso respuesta(String nombre){
        return new Curso(null, nombre, "General",new ArrayList<>());
    }
}
