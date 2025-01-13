package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.curso.DatosRegistroCurso;
import com.aluracursos.Foro.Hub.domain.curso.DatosRespuestaCurso;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRespuestaUsuario;
import com.aluracursos.Foro.Hub.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/buscar/{id}")
    public ResponseEntity<DatosRespuestaCurso> retornarDatosCurso(@PathVariable Long id) {
        Curso curso = cursoService.obtenerCurso(id);
        DatosRespuestaCurso datosRespuestaCurso = new DatosRespuestaCurso(curso);
        return ResponseEntity.ok(datosRespuestaCurso);
    }
}