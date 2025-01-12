package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.curso.DatosRegistroCurso;
import com.aluracursos.Foro.Hub.domain.curso.DatosRespuestaCurso;
import com.aluracursos.Foro.Hub.service.CursoService;
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
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping("/registrar")
    public ResponseEntity registrarCurso(@RequestBody @Valid DatosRegistroCurso datosRegistroCurso,
                                         UriComponentsBuilder uriComponentsBuilder) {

        DatosRespuestaCurso datosRespuestaCurso = cursoService.registrarCurso(datosRegistroCurso);
        URI url = uriComponentsBuilder.path("/curso/{id}").buildAndExpand(datosRespuestaCurso.id()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaCurso);
    }
}