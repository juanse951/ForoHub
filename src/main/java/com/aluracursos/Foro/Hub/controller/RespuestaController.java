package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import com.aluracursos.Foro.Hub.service.RespuestaService;
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
@RequestMapping("/respuesta")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuesta", description = "Controlador para gestionar Respuestas.")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @PostMapping("/registrar/{topicoId}")
    @Operation(
            summary = "Registrar una Respuesta",
            description = "Permite registrar una respuesta proporcionando el ID del topico, el mensaje y el ID del autor."
    )
    public ResponseEntity<DatosRespuestaRespuesta> registrarRespuesta(@Parameter(description = "ID del topico, que se le asignara esta respuesta")
                                                                      @PathVariable Long topicoId,
                                                                      @RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                                                                      UriComponentsBuilder uriBuilder) {

        Respuesta respuesta = respuestaService.agregarRespuesta(topicoId, datosRegistroRespuesta);
        DatosRespuestaRespuesta datosRespuesta = new DatosRespuestaRespuesta(respuesta);
        URI uri = uriBuilder.path("/respuesta/{id}").buildAndExpand(respuesta.getId()).toUri();

        return ResponseEntity.created(uri).body(datosRespuesta);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(
            summary = "Actualizar una Respuesta",
            description = "Permite actualizar los datos de una respuesta existente proporcionando su ID, el mensaje y una solucion valida: Pendiente, No solucionado, Solucionado."
    )
    public ResponseEntity<DatosRespuestaRespuesta> actualizarRespuesta(@Parameter(description = "ID de la respuesta a actualizar")
                                                                       @PathVariable Long id,
                                                                       @RequestBody @Valid DatosActualizarRespuesta datosActualizarRespuesta) {
        Respuesta respuesta = respuestaService.actualizarRespuesta(id, datosActualizarRespuesta);
        DatosRespuestaRespuesta datosRespuestaRespuesta = new DatosRespuestaRespuesta(respuesta);
        return ResponseEntity.ok(datosRespuestaRespuesta);
    }

    @GetMapping("/listado")
    @Operation(
            summary = "Lista de respuestas registradas",
            description = "Permite ver los datos de las respuestas existentes con su ID, mensaje, topico, fecha de creacion, autor y solucion."
    )
    public ResponseEntity<Page<DatosListadoRespuesta>> listadoRespuesta(Pageable paginacion) {
        Page<Respuesta> respuestas = respuestaService.obtenerListadoRespuesta(paginacion);
        return ResponseEntity.ok(respuestas.map(DatosListadoRespuesta::new));
    }

    @GetMapping("/buscar/{id}")
    @Operation(
            summary = "Busca una Respuesta",
            description = "Permite buscar una respuesta proporcionando el ID."
    )
    public ResponseEntity<DatosRespuestaRespuesta> retornarDatosRespuesta(@Parameter(description = "ID de la respuesta a buscar")
                                                                          @PathVariable Long id) {
        Respuesta respuesta = respuestaService.obtenerRespuesta(id);
        DatosRespuestaRespuesta datosRespuestaRespuesta = new DatosRespuestaRespuesta(respuesta);
        return ResponseEntity.ok(datosRespuestaRespuesta);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(
            summary = "Elimina respuestas registradas",
            description = "Permite eliminar una respuesta existente por medio de su ID."
    )
    public ResponseEntity<String> eliminarRespuesta(@Parameter(description = "ID de la respuesta a eliminar")
                                                    @PathVariable Long id) {
        respuestaService.eliminarRespuesta(id);
        return ResponseEntity.ok("La Respuesta con ID " + id + " fue eliminado exitosamente.");
    }
}

