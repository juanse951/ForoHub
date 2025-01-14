package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.curso.*;
import com.aluracursos.Foro.Hub.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

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

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DatosRespuestaCurso> actualizarCurso(@PathVariable Long id,
                                                                   @RequestBody @Valid DatosActualizarCurso datosActualizarCurso) {
        Curso curso = cursoService.actualizarCurso(id, datosActualizarCurso);
        DatosRespuestaCurso datosRespuestaCurso = new DatosRespuestaCurso(curso);
        return ResponseEntity.ok(datosRespuestaCurso);
    }

    @GetMapping("/listado")
    public ResponseEntity<Page<DatosListadoCurso>> listadoCurso(@PageableDefault(size = 10) Pageable paginacion) {
        Page<Curso> cursos = cursoService.obtenerListadoCurso(paginacion);
        return ResponseEntity.ok(cursos.map(DatosListadoCurso::new));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.ok("El Curso con ID " + id + " fue eliminado exitosamente.");
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> obtenerCategorias() {
        List<String> categorias = Arrays.stream(Categoria.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categorias);
    }

}