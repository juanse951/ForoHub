package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario actualizarUsuario(Long id, DatosActualizarUsuario datosActualizarUsuario) {
        Usuario usuario = usuarioRepository.findById(id).orElse(new Usuario());

        if (datosActualizarUsuario.nombre() != null && !datosActualizarUsuario.nombre().trim().isEmpty()) {
            usuario.setNombre(datosActualizarUsuario.nombre());
        }

        return usuarioRepository.save(usuario);
    }

    public DatosRespuestaUsuario registrarUsuario(DatosRegistroUsuario datosRegistroUsuario) {

        Usuario usuario = new Usuario(datosRegistroUsuario);
        DatosRespuestaUsuario datosRespuestaUsuario =
                new DatosRespuestaUsuario(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getCorreoElectronico(),
                        usuario.getPerfil());

        usuarioRepository.save(usuario);
        return datosRespuestaUsuario;
    }
}