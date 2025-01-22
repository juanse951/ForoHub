package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.topico.*;
import com.aluracursos.Foro.Hub.service.TopicoService;
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
@RequestMapping("/topico")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topico", description = "Controlador para gestionar Topicos.")
public class TopicoController {

    @Autowired
    TopicoService topicoService;

    @PostMapping("/registrar")
    @Operation(
            summary = "Registrar un Topico",
            description = "Permite registrar un topico proporcionando el titulo, mensaje, el ID del autor y el ID del curso."
    )
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = topicoService.crearTopico(datosRegistroTopico);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping("/listado")
    @Operation(
            summary = "Lista de topicos registrados",
            description = "Permite ver los datos de los topicos existentes con su ID, titulo, mensaje, fecha de creacion, status, autor y curso."
    )
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(Pageable paginacion) {
        Page<Topico> topicos = topicoService.obtenerListadoTopicos(paginacion);
        return ResponseEntity.ok(topicos.map(DatosListadoTopico::new));
    }

    @GetMapping("/buscar/{id}")
    @Operation(
            summary = "Busca un Topico",
            description = "Permite buscar un topico proporcionando el ID."
    )
    public ResponseEntity<DatosRespuestaTopico> retornarDatosTopico(@Parameter(description = "ID del topico a buscar")
                                                                    @PathVariable Long id) {
        Topico topico = topicoService.obtenerTopico(id);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        return ResponseEntity.ok(datosRespuestaTopico);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(
            summary = "Actualizar un Topico",
            description = "Permite actualizar los datos de un topico existente proporcionando su ID, titulo, mensaje, el ID del autor y el ID del curso."
    )
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@Parameter(description = "ID del topico a actualizar")
                                                                 @PathVariable Long id,
                                                                 @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        Topico topico = topicoService.actualizarTopico(id, datosActualizarTopico);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        return ResponseEntity.ok(datosRespuestaTopico);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(
            summary = "Elimina topicos registrados",
            description = "Permite eliminar un topico existente por medio de su ID."
    )
    public ResponseEntity<String> eliminarTopico(@Parameter(description = "ID del topico a eliminar")
                                                 @PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.ok("El tópico con ID " + id + " fue eliminado exitosamente.");
    }

}
