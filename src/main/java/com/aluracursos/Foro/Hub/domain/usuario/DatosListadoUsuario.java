package com.aluracursos.Foro.Hub.domain.usuario;

public record DatosListadoUsuario(

        Long id,

        String nombre,

        String correoElectronico,

        TipoPerfil perfil

) {

    public DatosListadoUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getPerfil()
        );
    }
}
