package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import com.aluracursos.Foro.Hub.service.RespuestaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuesta")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @PostMapping("/registrar/{topicoId}")
    public ResponseEntity<DatosRespuestaRespuesta> registrarRespuesta(@PathVariable Long topicoId,
                                                                      @RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                                                                      UriComponentsBuilder uriBuilder) {

        Respuesta respuesta = respuestaService.agregarRespuesta(topicoId, datosRegistroRespuesta);
        DatosRespuestaRespuesta datosRespuesta = new DatosRespuestaRespuesta(respuesta);
        URI uri = uriBuilder.path("/respuesta/{id}").buildAndExpand(respuesta.getId()).toUri();

        return ResponseEntity.created(uri).body(datosRespuesta);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DatosRespuestaRespuesta> actualizarRespuesta(@PathVariable Long id,
                                                                       @RequestBody @Valid DatosActualizarRespuesta datosActualizarRespuesta) {
        Respuesta respuesta = respuestaService.actualizarRespuesta(id, datosActualizarRespuesta);
        DatosRespuestaRespuesta datosRespuestaRespuesta = new DatosRespuestaRespuesta(respuesta);
        return ResponseEntity.ok(datosRespuestaRespuesta);
    }

    @GetMapping("/listado")
    public ResponseEntity<Page<DatosListadoRespuesta>> listadoRespuesta(@PageableDefault(size = 10) Pageable paginacion) {
        Page<Respuesta> respuestas = respuestaService.obtenerListadoRespuesta(paginacion);
        return ResponseEntity.ok(respuestas.map(DatosListadoRespuesta::new));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<DatosRespuestaRespuesta> retornarDatosRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaService.obtenerRespuesta(id);
        DatosRespuestaRespuesta datosRespuestaRespuesta = new DatosRespuestaRespuesta(respuesta);
        return ResponseEntity.ok(datosRespuestaRespuesta);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarRespuesta(@PathVariable Long id) {
        respuestaService.eliminarRespuesta(id);
        return ResponseEntity.ok("La Respuesta con ID " + id + " fue eliminado exitosamente.");
    }
}

