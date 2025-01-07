package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public record DatosRegistroPerfil(

        @NotBlank(message = "{nombre.obligatorio}")
        TipoPerfil nombre
) {
    public static Perfil registro(TipoPerfil nombre){
        if (nombre == null) {
            nombre = TipoPerfil.USER;  // Valor predeterminado
        }
        return new Perfil(null,nombre, new ArrayList<>());
    }
}
