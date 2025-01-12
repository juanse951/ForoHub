package com.aluracursos.Foro.Hub.domain.usuario;

public record DatosRespuestaUsuario(

        Long id,

        String nombre,

        String correoElectronico,

        TipoPerfil perfil

) {
}
