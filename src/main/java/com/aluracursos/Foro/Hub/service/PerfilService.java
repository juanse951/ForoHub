package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosRegistroPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.Perfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.PerfilRepository;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.TipoPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Perfil crearPerfil(DatosRegistroPerfil datos) {
        TipoPerfil perfilAsignado = TipoPerfil.USER;

        Perfil perfil =
          new Perfil(
              null,
              perfilAsignado,
              new ArrayList<>());

        return perfilRepository.save(perfil);
    }
}
