package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.respuesta.DatosRegistroRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.DatosRespuestaRespuesta;
import com.aluracursos.Foro.Hub.service.RespuestaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuesta")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @PostMapping("/registrar")
    public ResponseEntity registrarRespuesta(@RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                                             UriComponentsBuilder uriComponentsBuilder) {

        DatosRespuestaRespuesta datosRespuestaRespuesta = respuestaService.registroRespuesta(datosRegistroRespuesta);
        URI url = uriComponentsBuilder.path("/respuesta/{id}").buildAndExpand(datosRespuestaRespuesta.id()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaRespuesta);
    }

}
