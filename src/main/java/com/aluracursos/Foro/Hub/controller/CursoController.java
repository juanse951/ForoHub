package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.curso.*;
import com.aluracursos.Foro.Hub.service.CursoService;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/curso")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Curso", description = "Controlador para gestionar Cursos.")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping("/registrar")
    @Operation(
            summary = "Registrar un Curso",
            description = "Permite registrar un curso proporcionando el nombre y una categoria valida."
    )
    public ResponseEntity registrarCurso(@RequestBody @Valid DatosRegistroCurso datosRegistroCurso,
                                         UriComponentsBuilder uriComponentsBuilder) {

        DatosRespuestaCurso datosRespuestaCurso = cursoService.registrarCurso(datosRegistroCurso);
        URI url = uriComponentsBuilder.path("/curso/{id}").buildAndExpand(datosRespuestaCurso.id()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaCurso);
    }

    @GetMapping("/buscar/{id}")
    @Operation(
            summary = "Busca un Curso",
            description = "Permite buscar un curso proporcionando el ID."
    )
    public ResponseEntity<DatosRespuestaCurso> retornarDatosCurso(@Parameter(description = "ID del curso a buscar")
                                                                  @PathVariable Long id) {
        Curso curso = cursoService.obtenerCurso(id);
        DatosRespuestaCurso datosRespuestaCurso = new DatosRespuestaCurso(curso);
        return ResponseEntity.ok(datosRespuestaCurso);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(
            summary = "Actualizar un Curso",
            description = "Permite actualizar los datos de un curso existente proporcionando su ID, el nombre y una categoria valida."
    )
    public ResponseEntity<DatosRespuestaCurso> actualizarCurso(@Parameter(description = "ID del curso a actualizar")
                                                               @PathVariable Long id,
                                                               @RequestBody @Valid DatosActualizarCurso datosActualizarCurso) {
        Curso curso = cursoService.actualizarCurso(id, datosActualizarCurso);
        DatosRespuestaCurso datosRespuestaCurso = new DatosRespuestaCurso(curso);
        return ResponseEntity.ok(datosRespuestaCurso);
    }

    @GetMapping("/listado")
    @Operation(
            summary = "Lista de cursos registrados",
            description = "Permite ver los datos de los cursos existentes con su ID, nombre y categoria."
    )
    public ResponseEntity<Page<DatosListadoCurso>> listadoCurso(Pageable paginacion) {
        Page<Curso> cursos = cursoService.obtenerListadoCurso(paginacion);
        return ResponseEntity.ok(cursos.map(DatosListadoCurso::new));
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(
            summary = "Elimina cursos registrados",
            description = "Permite eliminar un curso existente por medio de su ID."
    )
    public ResponseEntity<String> eliminarCurso(@Parameter(description = "ID del curso a eliminar")
                                                @PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.ok("El Curso con ID " + id + " fue eliminado exitosamente.");
    }

    @GetMapping("/categorias")
    @Operation(
            summary = "Categorias disponibles para registrar tus cursos",
            description = "Permite ver la variedad de categorias en donde podria aplicar tu curso."
    )
    public ResponseEntity<List<String>> obtenerCategorias() {
        List<String> categorias = Arrays.stream(Categoria.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categorias);
    }

}