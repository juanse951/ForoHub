package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.usuario.*;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Controlador para gestionar Usuarios.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    @Operation(
            summary = "Registrar un Usuario",
            description = "Permite registrar un usuario proporcionando el nombre, correo electronico y una contraseña valida."
    )
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                           UriComponentsBuilder uriComponentsBuilder) {

        DatosRespuestaUsuario datosRespuestaUsuario = usuarioService.registrarUsuario(datosRegistroUsuario);
        URI url = uriComponentsBuilder.path("/usuario/{id}").buildAndExpand(datosRespuestaUsuario.id()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaUsuario);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(
            summary = "Actualizar un Usuario",
            description = "Permite actualizar los datos de un usuario existente proporcionando su ID, el nombre, el correo electronico y una contraseña valida."
    )
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(@Parameter(description = "ID del usuario a actualizar")
                                                                  @PathVariable Long id,
                                                                  @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario) {
        Usuario usuario = usuarioService.actualizarUsuario(id, datosActualizarUsuario);
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario);
        return ResponseEntity.ok(datosRespuestaUsuario);
    }

    @GetMapping("/listado")
    @Operation(
            summary = "Lista de usuarios registrados",
            description = "Permite ver los datos de los usuarios existentes con su ID, nombre, correo electronico y perfil."
    )
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios(Pageable paginacion) {
        Page<Usuario> usuarios = usuarioService.obtenerListadoUsuario(paginacion);
        return ResponseEntity.ok(usuarios.map(DatosListadoUsuario::new));
    }

    @GetMapping("/buscar/{id}")
    @Operation(
            summary = "Busca un Usuario",
            description = "Permite buscar un usuario proporcionando el ID."
    )
    public ResponseEntity<DatosRespuestaUsuario> retornarDatosUsuario(@Parameter(description = "ID del usuario a buscar")
                                                                      @PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuario(id);
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario);
        return ResponseEntity.ok(datosRespuestaUsuario);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(
            summary = "Elimina usuarios registrados",
            description = "Permite eliminar un usuario existente por medio de su ID."
    )
    public ResponseEntity<String> eliminarUsuario(@Parameter(description = "ID del usuario a eliminar")
                                                  @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("El Usuario con ID " + id + " fue eliminado exitosamente.");
    }
}
