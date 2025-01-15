package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.usuario.TipoPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

public record DatosActualizarPerfil(

        @Schema(example = "Déjalo en blanco para mantener o cambia a USER / MODERATOR / ADMIN")
        TipoPerfil perfil
) {
    public DatosActualizarPerfil(Usuario perfil){
        this(
                perfil.getPerfil()
        );
    }
}
