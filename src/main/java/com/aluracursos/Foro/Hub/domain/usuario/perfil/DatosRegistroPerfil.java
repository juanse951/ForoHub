package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public record DatosRegistroPerfil(

        @NotNull(message = "{nombre.obligatorio}")
        TipoPerfil nombre
) {
    public static Perfil registro(TipoPerfil nombre){
        if (nombre == null) {
            nombre = TipoPerfil.USER;  // Valor predeterminado
        }
        return new Perfil(null,nombre, new ArrayList<>());
    }
}
