package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.usuario.*;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosActualizarPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosPerfilRespuesta;
import com.aluracursos.Foro.Hub.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DatosPerfilRespuesta> actualizarUsuario(@PathVariable Long id,
                                                                   @RequestBody @Valid DatosActualizarPerfil datosActualizarPerfil) {
        Usuario usuario = usuarioService.actualizarPerfil(id, datosActualizarPerfil);
        DatosPerfilRespuesta datosPerfilRespuesta = new DatosPerfilRespuesta(usuario);
        return ResponseEntity.ok(datosPerfilRespuesta);
    }

    @GetMapping("/listado")
    public ResponseEntity<Page<DatosPerfilRespuesta>> obtenerIdNombresYPerfiles(@PageableDefault(size = 10) Pageable paginacion) {
        Page<DatosPerfilRespuesta> perfiles = usuarioService.obtenerIdNombresYPerfiles(paginacion);
        return ResponseEntity.ok(perfiles);
    }

}