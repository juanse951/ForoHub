package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public record DatosRegistroPerfil(

        @NotNull(message = "{nombre.obligatorio}")
        String nombre
) {
    public static Perfil registro(String nombre){
        if (nombre == null) {
            nombre = "prueba";  // Valor predeterminado
        }
        return new Perfil(null,nombre, new ArrayList<>());
    }
}
