package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import jakarta.validation.constraints.NotNull;

public record DatosRegistroPerfil(

        @NotNull(message = "{nombre.obligatorio}")
        TipoPerfil nombre
) {

}
