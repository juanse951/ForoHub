package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import com.aluracursos.Foro.Hub.domain.usuario.TipoPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;

public record DatosListadoPerfil(

        Long id,

        String nombre,

        TipoPerfil perfil
) {
    public DatosListadoPerfil(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPerfil()
        );
    }
}
