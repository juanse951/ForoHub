package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.usuario.DatosActualizarUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRegistroUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRespuestaUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                           UriComponentsBuilder uriComponentsBuilder) {

        DatosRespuestaUsuario datosRespuestaUsuario = usuarioService.registrarUsuario(datosRegistroUsuario);
        URI url = uriComponentsBuilder.path("/usuario/{id}").buildAndExpand(datosRespuestaUsuario.id()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaUsuario);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(@PathVariable Long id,
                                                                 @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario) {
        Usuario usuario = usuarioService.actualizarUsuario(id, datosActualizarUsuario);
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario);
        return ResponseEntity.ok(datosRespuestaUsuario);
    }

}
