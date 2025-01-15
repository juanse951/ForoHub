package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import com.aluracursos.Foro.Hub.domain.usuario.TipoPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;

public record DatosPerfilRespuesta(
        Long id,

        String nombre,

        TipoPerfil perfil
) {
    public DatosPerfilRespuesta(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPerfil()
        );
    }
}
