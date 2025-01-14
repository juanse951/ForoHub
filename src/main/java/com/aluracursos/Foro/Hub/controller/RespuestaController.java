package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.respuesta.DatosRegistroRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.DatosRespuestaRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.service.RespuestaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuesta")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @PostMapping("/{topicoId}")
    public ResponseEntity<DatosRespuestaRespuesta> registrarRespuesta(@PathVariable Long topicoId,
                                                                      @RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                                                                      UriComponentsBuilder uriBuilder) {

        Respuesta respuesta = respuestaService.agregarRespuesta(topicoId, datosRegistroRespuesta);

        DatosRespuestaRespuesta datosRespuesta = new DatosRespuestaRespuesta(respuesta);

        URI uri = uriBuilder.path("/respuesta/{id}").buildAndExpand(respuesta.getId()).toUri();

        return ResponseEntity.created(uri).body(datosRespuesta);
    }

}

