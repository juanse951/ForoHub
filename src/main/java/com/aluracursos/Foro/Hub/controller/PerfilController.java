package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.usuario.*;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosActualizarPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosListadoPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosPerfilRespuesta;
import com.aluracursos.Foro.Hub.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfil")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Perfil", description = "Controlador para gestionar Perfiles.")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/actualizar/{id}")
    @Operation(
            summary = "Actualizar un Perfil",
            description = "Permite actualizar los datos de un perfil existente proporcionando su ID y un tipo valido: USER/MODERATOR/ADMIN."
    )
    public ResponseEntity<DatosPerfilRespuesta> actualizarUsuario(@Parameter(description = "ID del perfil a actualizar")
                                                                   @PathVariable Long id,
                                                                   @RequestBody @Valid DatosActualizarPerfil datosActualizarPerfil) {
        Usuario usuario = usuarioService.actualizarPerfil(id, datosActualizarPerfil);
        DatosPerfilRespuesta datosPerfilRespuesta = new DatosPerfilRespuesta(usuario);
        return ResponseEntity.ok(datosPerfilRespuesta);
    }

    @GetMapping("/listado")
    @Operation(
            summary = "Lista de perfiles registrados",
            description = "Permite ver los datos de los perfiles existentes con su ID, nombre y tipo: USER/MODERATOR/ADMIN."
    )
    public ResponseEntity<Page<DatosListadoPerfil>> obtenerIdNombresYPerfiles(Pageable paginacion) {
        Page<Usuario> perfiles = usuarioService.obtenerIdNombresYPerfiles(paginacion);
        return ResponseEntity.ok(perfiles.map(DatosListadoPerfil::new));
    }

}