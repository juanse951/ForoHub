package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import com.aluracursos.Foro.Hub.domain.usuario.Usuario;

import java.util.ArrayList;

public record DatosRespuestaUsuario(

        String nombre,

        String correoElectronico,

        String contrasena
) {
    public static Usuario respuesta(String nombre){
        String correoElectronico = nombre.replaceAll(" ", "") + "@example.com";
        String contrasena = "defaultPassword";
        return new Usuario(null, nombre, correoElectronico, contrasena,
                null, new ArrayList<>(),new ArrayList<>());
    }
}
